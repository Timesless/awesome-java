package com.yangzl.spring;

/**
 * @author yangzl
 * @date 2021/3/29
 */
public interface InitializingBean {

	/**
	 * 设置属性
	 */
	void afterPropertySet();
}
