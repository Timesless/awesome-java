package com.yangzl.juc.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author yangzl
 * @date 2020/12/28 16:49
 *
 * 字段原子更新器，当字段为基本类型变量时使用
 * 
 * 		AtomicIntegerFieldUpdater
 * 		AtomicLongFieldUpdater
 * 		AtomicReferenceFieldUpdater	
 * 	
 */
public class AtomicFieldUpdaterD {
	
	private static final AtomicIntegerFieldUpdater<AtomicFieldUpdaterD> intUpdater =
			AtomicIntegerFieldUpdater.newUpdater(AtomicFieldUpdaterD.class, "totalCount");
	private static final AtomicLongFieldUpdater<AtomicFieldUpdaterD> longUpdater = 
			AtomicLongFieldUpdater.newUpdater(AtomicFieldUpdaterD.class, "totalScore");
	private static final AtomicReferenceFieldUpdater<AtomicFieldUpdaterD, Integer> refUpdater = 
			AtomicReferenceFieldUpdater.newUpdater(AtomicFieldUpdaterD.class, Integer.class, "currentScore");
	
	public volatile int totalCount = 0;
	public volatile long totalScore = 0;
	public volatile Integer currentScore = 0;

	public static void main(String[] args) {
		
		int LOOP = 100;
		final AtomicFieldUpdaterD client = new AtomicFieldUpdaterD();
		CountDownLatch latch = new CountDownLatch(LOOP);
		for (int i = 0; i < LOOP; i++) {
			int fi = i;
			new Thread(() -> {
				intUpdater.incrementAndGet(client);
				longUpdater.incrementAndGet(client);
				refUpdater.getAndAccumulate(client, 2, Integer::sum);

				latch.countDown();
			}).start();
		}
		
		try {
			latch.await();
		} catch(InterruptedException e) {
		  	e.printStackTrace();
		}

		System.out.printf("totalCount is: %d, totalScore is: %d, currentScore is: %d\n",
				intUpdater.get(client), longUpdater.get(client), refUpdater.get(client));
	}

}
