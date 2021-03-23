package com.yangzl.interview3;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yangzl
 * @date 2021/3/21
 * @desc LockSupport 基本使用
 * 		1. permit 无法累加最多为 1
 * 		2. park 消耗一个许可证
 * 		3. unpark 获得一个许可证
 * 		4. 连续 unpark 无效
 */
public class LockSup {

	Object monitor = new Object();
	Lock lock = new ReentrantLock();
	Condition condition = lock.newCondition();

	/**
	 * before
	 * 	synchronized + wait + notify
	 *
	 * 	1. 必须先阻塞，再唤醒
	 * 	2. 必须在 synchronized 块内，否则 IllegalMonitorStateException
	 */
	@Test
	public void testWaitNotify() {

		new Thread(() -> {
			synchronized (monitor) {
				String threadName = Thread.currentThread().getName();
				System.out.println(threadName + " in ...");
				try {
					TimeUnit.SECONDS.sleep(1);
					monitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(threadName + " out ...");
			}
		}, "A").start();

		new Thread(() -> {
			synchronized (monitor) {
				monitor.notify();
				System.out.println(Thread.currentThread().getName() + " notify ...");
			}
		}, "B").start();

		try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
	}

	/**
	 * before
	 * Lock + await + signal
	 *
	 * 1. 必须先阻塞再唤醒
	 * 2. 必须与 Lock 结合使用，否则 IllegalMonitorStateException
	 */
	@Test
	public void testAwaitSignal() {
		new Thread(() -> {
			lock.lock();
			try {
				System.out.println(Thread.currentThread().getName() + " in ...");
				TimeUnit.SECONDS.sleep(1);
				condition.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
			System.out.println(Thread.currentThread().getName() + " out ...");
		}, "A").start();

		new Thread(() -> {
			lock.lock();
			try {
				condition.signal();
				System.out.println(Thread.currentThread().getName() + " signal ...");
			} finally {
				lock.unlock();
			}
		}, "B").start();

		try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
	}

	/**
	 * after
	 * LockSupport 使用
	 */
	@Test
	public void testLockSupport() {

		Thread A = new Thread(() -> {
			String threadName = Thread.currentThread().getName();
			System.out.println(threadName + " in ...");
			LockSupport.park();
			System.out.println(threadName + " out ...");
		}, "A");
		A.start();

		new Thread(() -> {
			try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
			LockSupport.unpark(A);
			System.out.println(Thread.currentThread().getName() + " unpark A ...");
		}, "B").start();

		try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
	}

}
