package cn.leetcode.thread.printinorder;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @Author: yangzl
 * @Date: 2020/1/18 09:26
 * @Desc: ..
 * 
 * 我们提供了一个类：
 * public class Foo {
 *   public void one() { print("one"); }
 *   public void two() { print("two"); }
 *   public void three() { print("three"); }
 * }
 * 三个不同的线程将会共用一个 Foo 实例。
 *
 * 线程 A 将会调用 one() 方法
 * 线程 B 将会调用 two() 方法
 * 线程 C 将会调用 three() 方法
 * 请设计修改程序，以确保 two() 方法在 one() 方法之后被执行，three() 方法在 two() 方法之后被执行。
 *
 * 示例 1:
 * 输入: [1,2,3]
 * 输出: "onetwothree"
 * 解释: 
 * 有三个线程会被异步启动。
 * 输入 [1,2,3] 表示线程 A 将会调用 one() 方法，线程 B 将会调用 two() 方法，线程 C 将会调用 three() 方法。
 * 正确的输出是 "onetwothree"。
 * 
 * 示例 2:
 * 输入: [1,3,2]
 * 输出: "onetwothree"
 * 解释: 
 * 输入 [1,3,2] 表示线程 A 将会调用 one() 方法，线程 B 将会调用 three() 方法，线程 C 将会调用 two() 方法。
 * 正确的输出是 "onetwothree"。
 */
public class PrintInorderSemaphore {
	
	/*
	 * 1. 单信号量 构造为公平锁
	 * 2. 使用两个信号量控制
	 */
	private Semaphore sph = new Semaphore(0, true);
	public PrintInorderSemaphore() {}
	
	public void first(Runnable r1) {
		r1.run();
		sph.release(1);
	}
	
	public void second(Runnable r2) throws InterruptedException {
		sph.acquire(1);
		r2.run();
		sph.release(2);
	}
	
	public void third(Runnable r3) throws InterruptedException {
		sph.acquire(2);
		r3.run();
	}

	public static void main(String[] args) {

		PrintInorderSemaphore print = new PrintInorderSemaphore();
		new Thread(() -> {
			try {
				print.third(() -> System.out.println("three"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
		new Thread(() -> {
			try {
				print.second(() -> System.out.println("two"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		new Thread(() -> { print.first(() -> System.out.println("one")); }).start();
	}
	
}

