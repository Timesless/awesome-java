package com.yangzl.spring;

/**
 * @author yangzl
 * @date 2021/3/29
 */
public interface BeanPostProcessor {

	/**
	 * 后置处理器 before
	 *
	 * @param bean bean
	 * @param beanName beanname
	 * @return bean
	 */
	Object postProcessorBeforeInitialization(Object bean, String beanName);

	/**
	 * 后置处理器 after
	 *
	 * @param bean bean
	 * @param beanName beanname
	 * @return bean
	 */
	Object postProcessorAfterInitialization(Object bean, String beanName);
}
