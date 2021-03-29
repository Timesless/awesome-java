package com.yangzl.service;

import com.yangzl.spring.InitializingBean;
import com.yangzl.spring.annotation.Autowired;
import com.yangzl.spring.annotation.Component;
import com.yangzl.spring.aware.BeanNameAware;
import lombok.ToString;

/**
 * @author yangzl
 * @date 2021/3/29
 * @desc
 */
@ToString
@Component
public class UserService implements InitializingBean, BeanNameAware {

	private String beanName;

	@Autowired
	private OrderService orderService;

	@Override
	public void afterPropertySet() {
		System.out.println("==== UserService After Property Set");
	}

	@Override
	public void setBeanName(String beanName) {
		System.out.println("==== UserService BeanNameAware ");
		this.beanName = beanName;
	}
}
