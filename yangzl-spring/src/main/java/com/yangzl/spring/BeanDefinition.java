package com.yangzl.spring;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yangzl
 * @date 2021/3/28
 * @desc
 */

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class BeanDefinition {

	public BeanDefinition() {}

	private String scope;
	private Class<?> beanClass;
	private String beanClassName;
	private Boolean lazy;
}
