package com.yangzl;

import java.io.IOException;

/**
 * @author yangzl
 * @date 2020/11/5 09:47
 * @desc 启动HSDB：java -cp sa-jdi.jar sun.jvm.hotspot.HSDB
 */
public class HSDBTest {
	public static void main(String[] args) throws IOException {
		A obj = new B();
		System.in.read();
		System.out.println(obj);
	}
}

abstract class A {
	public void print() {
		System.out.println("I Love Vim");
	}
	
	public abstract void sayHello();
}

class B extends A {
	@Override
	public void sayHello() {
		
	}
}
