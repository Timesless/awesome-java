package com.yangzl.provider.impl;

import com.yangzl.provider.api.HelloService;

/**
 * @author yangzl
 * @date 2021/3/29
 *
 */
public class HelloServiceImpl implements HelloService {

	@Override
	public void helloDubbo() {
		System.out.println("==== hello dubbo ");
	}
}
