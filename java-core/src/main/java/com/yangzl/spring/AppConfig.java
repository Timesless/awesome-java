package com.yangzl.spring;

import com.yangzl.spring.config.A;
import com.yangzl.spring.config.B;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangzl
 * @date 2020/11/15 10:11
 * @desc
 */
@Configuration
public class AppConfig {

	public static void main(String[] args) {
		ApplicationContext app = new AnnotationConfigApplicationContext(AppConfig.class);
		System.out.println(app.getBean(A.class));
	}
	
	@Bean("a")
	public A a() {
		return new A();
	}
	
	@Bean("b")
	public B loadB() {
		return new B();
	}
}
