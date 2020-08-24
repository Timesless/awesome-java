package com.yangzl.juc.communication;

import java.util.concurrent.CyclicBarrier;

/**
 * @Author: yangzl
 * @Date: 2020/1/4 23:36
 * @Desc: .. 集齐七颗龙珠召唤神龙，回环屏障
 **/
public class CyclicBarrierD {

	public static void main(String[] args) {
		// CyclicBarrier(int parties, Runnable barrierAction)
		CyclicBarrier barrier = new CyclicBarrier(7,
				() -> System.out.println("召唤神龙"));
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
