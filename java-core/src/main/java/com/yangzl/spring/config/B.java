package com.yangzl.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yangzl
 * @date 2020/11/15 00:09
 * @desc Spring循环依赖测试bean
 */
@Component
public class B {
    
    private A a;
    @Autowired
    public void setA(A a) {
		System.out.println("B 中注入属性 A");
        this.a = a;
    }

    public B() {
		System.out.println("B 初始化成功....");
    }
}
