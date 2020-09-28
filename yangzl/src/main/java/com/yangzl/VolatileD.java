package com.yangzl;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @Author: yangzl
 * @Date: 2020/1/5 19:32
 * @Desc: ..	volatile 可见性，禁止指令重排。不保证原子性
 */
public class VolatileD {
	
	volatile int num = 10;
	void changeNum() {
		this.num = 20;
	}

	public static void main(String[] args) {
		VolatileD d = new VolatileD();
		new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			d.changeNum();
		}, "A").start();
		while (d.num == 10) {  }
		System.out.println("mission complete...");
	}
	
	@Test
	public void test1() {
		String[] str = {"123", "456"};
	}
}
