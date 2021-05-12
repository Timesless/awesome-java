package com.yangzl.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yangzl
 * @date 2021/3/28
 *
 * 模拟 spring ComponentScan 注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ComponentScan {
	String value() default "";
	// multi packages
	// String[] basePackages() default {};
}
