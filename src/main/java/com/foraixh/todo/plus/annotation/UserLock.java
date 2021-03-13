package com.foraixh.todo.plus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对用户的todoTaskList和todoTask的操作的锁
 * @author myvina@qq.com
 * @date 2021/3/13  14:17
 * @usage
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLock {
    /**
     * 用户名参数所在的索引位置
     * @return 索引位置
     */
    int paramIndex() default 0;
}
