package com.foraixh.todo.plus.configuration;

import com.foraixh.todo.plus.response.GlobalResponse;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

/**
 * @author myvina
 * @date 2021/03/10 11:29
 * @usage
 */
public class TodoGsonHttpMessageConverter extends GsonHttpMessageConverter {
    @Override
    protected boolean supports(Class<?> clazz) {
        return GlobalResponse.class.equals(clazz);
    }
}
