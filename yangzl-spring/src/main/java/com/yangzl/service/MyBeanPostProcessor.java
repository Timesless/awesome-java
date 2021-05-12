package com.yangzl.service;

import com.yangzl.spring.BeanPostProcessor;
import com.yangzl.spring.annotation.Component;

/**
 * @author yangzl
 * @date 2021/3/29
 *
 * Spring Bean 后置处理器
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessorBeforeInitialization(Object bean, String beanName) {
		System.out.println(beanName + "=== 初始化前 ===");
		return bean;
	}

	@Override
	public Object postProcessorAfterInitialization(Object bean, String beanName) {
		System.out.println(beanName + "=== 初始化后 ===");
		return bean;
	}
}
