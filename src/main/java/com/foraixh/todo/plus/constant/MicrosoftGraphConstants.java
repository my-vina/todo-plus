package com.foraixh.todo.plus.constant;

/**
 * @author myvina
 * @date 2021/03/10 17:31
 * @usage
 */
public interface MicrosoftGraphConstants {
    String AUTHORITY = "https://login.microsoftonline.com/common/";

    String DEVICE_LOGIN_URL = "https://microsoft.com/devicelogin";

    String TOKEN_REDIS_MAP = "TOKEN_REDIS_MAP";
    String TOKEN_REDIS_SCHEDULE_REFRESH_QUEUE = "TOKEN_REDIS_SCHEDULE_REFRESH_QUEUE";

    String ACCESS_TOKEN = "accessToken";
    String REFRESH_TOKEN = "refreshToken";
    String EXPIRES_ON = "expiresOn";
}
