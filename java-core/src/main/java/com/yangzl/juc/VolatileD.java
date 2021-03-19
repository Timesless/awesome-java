package com.yangzl.juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yangzl
 * @date 2020/1/5 19:32
 * @desc ..	volatile 可见性，禁止指令重排。不保证原子性
 */
public class VolatileD {

	// int num = 0

	volatile int num = 0;

	/**
	 * 修改值
	 * 	测试可见性
	 */
	void changeNum() {
		this.num = 20;
	}

	/**
	 * 自增
	 * 	测试原子性
	 * 	void synchronized incrNum();
	 *
	 * 	num ++ 字节码
	 * 	aload0
	 * 	dup
	 * 	getfield
	 * 	iconst1
	 * 	iadd
	 * 	putfield
	 */
	void incrNum() {
		num ++;
	}

	/**
	 * 1. volatile 保证可见性
	 */
	@Test
	public void testVisiblity() {
		VolatileD d = new VolatileD();
		new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			d.changeNum();
		}, "A").start();
		// 线程 A 更改共享变量后 main 线程无法感知
		while (d.num == 0) {
			;
		}
		System.out.println("mission complete...");
	}

	/**
	 * 2. volatile 不保证原子性
	 */
	@Test
	public void testUnAtomic() {
		VolatileD demo = new VolatileD();
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				for (int j = 0; j < 2000; j++) {
					demo.incrNum();
				}
			}).start();
		}
		// 让 incrNum 执行完毕
		if (Thread.activeCount() > 2) {
			Thread.yield();
		}
		// try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
		System.out.println("10 threads add 20000 times num = " + demo.num);
	}

	/**
	 * 原子性 ++
	 */
	@Test
	public void testAtomic() {
		AtomicInteger num = new AtomicInteger();
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				for (int j = 0; j < 2000; j++) {
					num.getAndIncrement();
				}
			}).start();
		}
		try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
		System.out.println("10 threads add 20000 times num = " + num.get());
	}
}
