package com.yangzl.jvm;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author yangzl
 * @date 2020/12/22 22:18
 * @desc jconsole 测试JConsole线程页签
 * 
 * main线程处于Runnable，堆栈追踪到readBytes()，检测到流没有更新立刻将CPU归还給OS
 */

public class JConsole {

	/**
	 * 创建忙线程使用JConsole观察，这种情况是忙循环，不归还CPU资源
	 * 
	 * @return void
	 */
	static void createBusy() {
		new Thread(() -> {
			while (true)
				;
		}, "busyThread").start();
	}
	
	/**
	 * 创建等待线程使用JConsole观察
	 * 
	 * @param monitor 监视器对象
	 */
	static void createLock(final Object monitor) {
		new Thread(() -> {
			synchronized (monitor) {
				try {
					monitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "lockThread").start();
	}
	
	/**
	 * 创建死锁
	 * 由于Integer.valueOf缓存「-128, 127」所以多个线程加的锁是同一个对象
	 */
	static void createDead(int a, int b) {
		synchronized (Integer.valueOf(a)) {
			synchronized (Integer.valueOf(b)) {
				System.out.printf("a + b is %d\n", a + b);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		br.readLine();
		createBusy();
		br.readLine();
		final Object monitor = new Object();
		createLock(monitor);
		
		// 多创建线程以至于进入死锁条件
		for (int i = 0; i < 20; ++i) {
			new Thread(() -> createDead(1, 2)).start();
			new Thread(() -> createDead(1, 2)).start();
		}
	}
}
