package com.foraixh.todo.plus.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author myvina
 * @date 2021/03/10 11:19
 * @usage
 */

@Configuration
public class TodoWebMvcConfigurerAdapter implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        GsonHttpMessageConverter gsonHttpMessageConverter = new TodoGsonHttpMessageConverter();
        converters.add(0, gsonHttpMessageConverter);
    }
}
