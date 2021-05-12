package com.yangzl.juc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author yangzl
 * @date 2020/1/4 23:47
 *
 * 信号量
 * 			1. 资源互斥
 * 			2. 控制并发线程数「做限流，同时 10w 请求只放入 1000，最后这 1000 再抢 100 台手机」
 * 	注意在 finally 块中释放 permits
 */
public class SemaphoreD {

	public static void main(String[] args) {
		/*
		 * Semaphore(int permits)
		 * 当permits = 1，为普通资源类，可当作 synchronized / ReentrantLock 使用
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
			}, "t" + i).start();
		}
	}
}
