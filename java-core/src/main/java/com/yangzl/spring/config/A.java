package com.yangzl.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yangzl
 * @date 2020/11/15 00:09
 * @desc Spring循环依赖 测试bean
 */

@Component
public class A {
    
    private B b;
    @Autowired
    public void setB(B b) {
		System.out.println("A 中注入属性 B");
        this.b = b;
    }
    
    public A() {
		System.out.println("A 初始化成功...");
    }
    
}
