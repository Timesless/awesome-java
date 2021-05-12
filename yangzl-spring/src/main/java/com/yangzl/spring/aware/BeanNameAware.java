package com.yangzl.spring.aware;

/**
 * @author yangzl
 * @date 2021/3/29
 */
public interface BeanNameAware {
	/**
	 * 如果 bean 实现了该接口，则給 bean 设置 beanName
	 *
	 * @param beanName name
	 */
	void setBeanName(String beanName);
}
