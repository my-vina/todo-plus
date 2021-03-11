package com.foraixh.todo.plus.service;

import com.microsoft.aad.msal4j.IAuthenticationResult;

/**
 * @author myvina@qq.com
 * @date 2021/3/12  6:56
 * @usage 对token进行存储和定时刷新，以及从redis获取
 */

public interface TokenService {
    /**
     * 通过用户名从redis里获取token
     * @param userName 用户名
     * @return token
     */
    String getTokenByUserName(String userName);

    /**
     * 成功登陆后，将token存储到redis，然后定时刷新缓存
     * @param result 登陆成功后认证信息
     */
    void tokenStorageScheduleRefresh(IAuthenticationResult result);

    /**
     * token刷新
     * @param refreshToken 用于刷新的token
     */
    void refreshToken(String refreshToken);
}
