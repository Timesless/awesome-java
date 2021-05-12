package com.yangzl.spring;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yangzl
 * @date 2021/3/28
 *
 * bean 定义文件
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class BeanDefinition {

	public BeanDefinition() {}
	/** 作用域 singleton / prototype */
	private String scope;
	/** bean 所属的类 spring 用此反射生成对象 */
	private Class<?> beanClass;
	/** beanName */
	private String beanClassName;
	/** 是否 lazy bean */
	private Boolean lazy;
}
