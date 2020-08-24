package com.yangzl.juc.communication;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * @Author: yangzl
 * @Date: 2020/1/4 23:25
 * @Desc: .. 
 **/
public class CountDownLatchD {
	
	public static void main(String[] args) {
		// countDownLatchClose();
		closeDoor();
	}

	// join可以等待线程执行完毕
	private static void closeDoor() {
		IntStream.rangeClosed(0, 5).forEach(
				x -> {
					Thread t = new Thread(() -> System.out.println("1人离开教室..."));
					try {
						t.start();
						t.join();
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
		);
		System.out.println("班长关门...");
	}

	/**
	 * 2020/6/7 使用CounctDownLatch完成
	 * @param
	 * @return
	 */
	private static void countDownLatchClose() {
		CountDownLatch latch = new CountDownLatch(5);
		for (int x = 0; x < 5; ++x) {
			new Thread(() -> {
				System.out.println("1人离开教室...");
				latch.countDown();
			}).start();
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("班长关门...");
	}
}
