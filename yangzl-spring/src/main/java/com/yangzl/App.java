package com.yangzl;

import com.yangzl.spring.ApplicationContext;
import com.yangzl.spring.annotation.ComponentScan;

/**
 * Hello world!
 *
 */

@ComponentScan("com.yangzl.service")
public class App {

    public static void main( String[] args ) {
		ApplicationContext ctx = new ApplicationContext(App.class);
		System.out.println(ctx.getBean("OrderService"));
		System.out.println(ctx.getBean("OrderService"));

		System.out.println(ctx.getBean("UserService"));
		System.out.println(ctx.getBean("UserService"));
	}
}
