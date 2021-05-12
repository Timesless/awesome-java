package com.yangzl.thread.printinorder;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author yangzl
 * @date 2020/11/14 10:02
 *
 * 线程按序打印 -> 信号量实现
 * TODO 在 leetcode 这个解法会超时， 难道说他不是启动的三个线程
 */

public class PrintInorderSemaphore {

	final Semaphore s = new Semaphore(0);
	public PrintInorderSemaphore() {}

	public void printOne(Runnable r) {
		try {
			s.acquire(0);
			r.run();
			s.release(1);
			System.out.printf("t1 执行后，剩余permits：%d \n", s.availablePermits());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void printTwo(Runnable r) {
		try {
			s.acquire(1);
			r.run();
			s.release(2);
			System.out.printf("t2 执行后，剩余permits：%d \n", s.availablePermits());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void printThree(Runnable r) {
		try {
			s.acquire(2);
			r.run();
			s.release(2);
			System.out.printf("t3 执行后，剩余permits：%d \n", s.availablePermits());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		PrintInorderSemaphore client = new PrintInorderSemaphore();

		new Thread(() -> client.printThree(() -> System.out.println("three"))).start();
		
		try { TimeUnit.MILLISECONDS.sleep(100); } catch(InterruptedException e) { e.printStackTrace(); }
		new Thread(() -> client.printOne(() -> System.out.println("one"))).start();

		try { TimeUnit.MILLISECONDS.sleep(100); } catch(InterruptedException e) { e.printStackTrace(); }
		new Thread(() -> client.printTwo(() -> System.out.println("two"))).start();
	}
}
