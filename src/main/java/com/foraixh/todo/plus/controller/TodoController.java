package com.foraixh.todo.plus.controller;

import com.foraixh.todo.plus.constant.MicrosoftGraphConstants;
import com.foraixh.todo.plus.service.TodoListService;
import com.foraixh.todo.plus.service.TokenService;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.microsoft.aad.msal4j.DeviceCodeFlowParameters;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.PublicClientApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author myvina
 * @date 2021/03/10 17:29
 * @usage
 */

@RestController
@RequestMapping("/todo")
public class TodoController {
    private static final Logger log = LoggerFactory.getLogger(TodoController.class);
    private final Gson gson = new Gson();

    private final PublicClientApplication pca;
    private final TodoListService todoListService;
    private final TokenService tokenService;

    @Value("${todo-plus.app.scopes}")
    private String[] scopes;

    public TodoController(PublicClientApplication pca, TodoListService todoListService, TokenService tokenService) {
        this.pca = pca;
        this.todoListService = todoListService;
        this.tokenService = tokenService;
    }

    @RequestMapping("/login")
    public void login(final HttpServletRequest request, final HttpServletResponse response) {
        Set<String> scopeSet = Sets.newHashSet(scopes);
        try {
            response.sendRedirect(MicrosoftGraphConstants.DEVICE_LOGIN_URL);
        } catch (IOException e) {
            log.error("跳转到设备编码登陆页面失败", e);
        }

        pca.acquireToken(DeviceCodeFlowParameters.builder(scopeSet, deviceCodeParam -> {
            // TODO 将设备编码发到对应的邮箱
            System.out.println(deviceCodeParam.message());
        }).build()).exceptionally(e -> {
            log.error("Unable to authenticate - {}", e.getMessage(), e);
            return null;
        }).thenAcceptAsync((IAuthenticationResult result) -> {
            tokenService.tokenStorageScheduleRefresh(result);
            todoListService.simpleSyncTodo(result.account().username());
        });
    }
}
