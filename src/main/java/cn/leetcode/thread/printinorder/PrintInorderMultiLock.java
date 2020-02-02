package cn.leetcode.thread.printinorder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: yangzl
 * @Date: 2020/1/18 09:26
 * @Desc: ..
 */
public class PrintInorderMultiLock {

	private Lock lock = new ReentrantLock();
	private Condition one = lock.newCondition();
	private Condition two = lock.newCondition();
	private Condition three = lock.newCondition();
	int state = 1;
	
	public PrintInorderMultiLock() { }
	public void first(Runnable printFirst) throws InterruptedException {
		lock.lock();
		try {
			while (state != 1) { one.await(); }
			state = 2;
			printFirst.run();
			two.signal();
		} finally {
			lock.unlock();
		}
	}
	public void second(Runnable printSecond) throws InterruptedException {
		lock.lock();
		try {
			while (state != 2) { two.await(); }
			state = 3;
			printSecond.run();
			three.signal();
		} finally {
			lock.unlock();
		}
	}
	public void third(Runnable printThird) throws InterruptedException {
		lock.lock();
		try {
			while (state != 3) { three.await(); }
			state = 1;
			printThird.run();
			// one.signal();
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {

		PrintInorderMultiLock cdl = new PrintInorderMultiLock();
		new Thread(() -> {
			try {
				cdl.third(() -> System.out.println("three"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
		new Thread(() -> {
			try {
				cdl.second(() -> System.out.println("two"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		new Thread(() -> {
			try {
				cdl.first(() -> System.out.println("one"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
}

