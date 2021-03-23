package com.yangzl.interview3;

/**
 * @author yangzl
 * @date 2021/3/21
 * @desc AQS（AbstractQueuedSynchronizer，抽象队列同步器）源码解析
 *
 *	1. ReentrantLock
 *	2. CountDownLatch
 * 	3. Semaphore
 * 	4. CyclicBarrier
 * 		都有一个静态内部类：abstract static class Sync extends AbstractQueuedSynchronizer {}
 */
public class AQS {

	static final int a = 0;

	public AQS(int a) {
		this.a = a;
	}
}
