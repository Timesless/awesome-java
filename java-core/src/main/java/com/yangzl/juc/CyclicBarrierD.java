package com.yangzl.juc;

import java.util.concurrent.CyclicBarrier;

/**
 * @author yangzl
 * @date 2020/1/4 23:36
 * @desc CyclicBarrier「回环屏障」，所有线程都到达栅栏时放行
 * 		集齐七颗龙珠召唤神龙
 */
public class CyclicBarrierD {

	public static void main(String[] args) {
		// CyclicBarrier(int parties, Runnable barrierAction)
		CyclicBarrier barrier = new CyclicBarrier(7, () -> System.out.println("召唤神龙"));
		for (int i = 0; i < 7; i++) {
			new Thread(() -> {
				System.out.println("收集一颗龙珠...");
				try {
					barrier.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		}
	}
}
