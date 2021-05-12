package com.yangzl.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yangzl
 * @date 2020/11/14 12:40
 *
 * AbstractQueuedSynchronizer 详解
 * 
 * AQS debug 源码
 * 第一个抢占的线程走 if 逻辑获取到锁，执行逻辑
 * 第二个线程走 acquire 去调用 （fariAcquire / nonFairAcquire）
 * final void lock() {
 *    if (compareAndSetState(0, 1))
 *    	setExclusiveOwnerThread(Thread.currentThread());
 *    else
 *    	acquire(1);
 *    }
 */
public class AQS {
	
	final ReentrantLock lock = new ReentrantLock();
	public void aqsProcess() {
		lock.lock();
		try {
			// 挂起，不释放锁
			LockSupport.park(this);
		} finally {
		  lock.unlock();
		}
	}

	public static void main(String[] args) {
		AQS client = new AQS();
		new Thread(client::aqsProcess, "A").start();
		// 3s 以后启动另一个线程，完成整个从tryAcquire到CLH双向队列中挂起的逻辑
		try { TimeUnit.SECONDS.sleep(4); } catch(InterruptedException e) { e.printStackTrace(); }
		new Thread(client::aqsProcess, "B").start();
	}

}
