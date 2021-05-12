package com.yangzl.spring.aware;

import com.yangzl.spring.ApplicationContext;

/**
 * @author yangzl
 * @date 2021/3/29
 *
 * 模拟 spring 各种 aware 接口
 */
public interface ApplicationContextAware {

	/**
	 * aware appContext
	 *
	 * @param applicatinContext context
	 */
	void setApplicatinContext(ApplicationContext applicatinContext);
}
