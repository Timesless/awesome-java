package com.yangzl.juc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @Author yangzl
 * @Date 2020/1/4 23:47
 * @Desc ..	多资源互斥使用，控制并发线程数
 */
public class SemaphoreD {

	public static void main(String[] args) {
		/*
		 * Semaphore(int permits)
		 * 当permits = 1，为普通资源类
		 */
		Semaphore semaphore = new Semaphore(3);
		for (int i = 0; i < 5; i++) {
			new Thread(() -> {
				try {
					semaphore.acquire();
					System.out.println(Thread.currentThread().getName() + "抢到车位...");
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println(Thread.currentThread().getName() + "离开...");
					semaphore.release();
				}
			}, "t" + (i+1)).start();
		}
	}
}
