package com.yangzl.spring.aware;

import com.yangzl.spring.ApplicationContext;

/**
 * @author yangzl
 * @date 2021/3/29
 * @desc
 */
public interface ApplicationContextAware {

	/**
	 * aware appContext
	 *
	 * @param applicatinContext context
	 */
	void setApplicatinContext(ApplicationContext applicatinContext);
}
