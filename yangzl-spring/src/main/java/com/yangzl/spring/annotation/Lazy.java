package com.yangzl.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yangzl
 * @date 2021/3/29
 *
 * 标注了该注解的 bean 是 lazy bean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Lazy {

	boolean value() default false;
}
