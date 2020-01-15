package com.yinhai.juc.clh;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @Author: yangzl
 * @Date: 2020/1/15 11:14
 * @Desc: ..
 */
public class ClhTest {

	static int count = 0;
	public static void testLock(Lock lock) {
		try {
			lock.lock();
			for (int i = 0; i < 10000; ++i) ++count;
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		final ClhSpinLock clh = new ClhSpinLock();
		final CyclicBarrier cb = new CyclicBarrier(2, () -> System.out.print(count));
		for (int i = 0; i < 2; i++) {
			new Thread(() -> {
				try {
					TimeUnit.MILLISECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
				testLock(clh);
					try {
						cb.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
			}, "t" + i).start();
		}
	}
}
