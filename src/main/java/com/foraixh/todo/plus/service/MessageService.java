package com.foraixh.todo.plus.service;

/**
 * @author myvina
 * @date 2021/03/15 19:57
 * @usage 消息通知服务
 */
public interface MessageService {
    /**
     * 将设备吗通知到用户
     * @param userName userName
     * @param deviceMessage deviceCode
     */
    void notify(String userName, String deviceMessage);
}
