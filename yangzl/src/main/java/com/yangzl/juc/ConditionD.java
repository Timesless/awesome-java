package com.yangzl.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: yangzl
 * @Date: 2020/1/4 20:51
 * @Desc: .. 
 * 		Interface Lock
 * 	ReentrantLock, ReentrantReadWriteLock.ReadLock, ReentrantLockReadWriteLock.WriteLock
 * 		非公平可重入递归锁
 */
public class ConditionD {

	// 标识位
	private int flag = 1;
	private final Lock lock = new ReentrantLock();
	private final Condition five = lock.newCondition();
	private final Condition ten = lock.newCondition();
	private final Condition fif = lock.newCondition();
	
	private void printFive() {
		lock.lock();
		try {
			// 注意使用wihle而不是if
			while (flag != 1) { five.await(); }
			flag = 2;
			System.out.println(5);
			ten.signal();
		} catch (InterruptedException e) {
			flag = 2;
		} finally {
			lock.unlock();
		}
	}
	
	private void printTen() {
		lock.lock();
		try {
			while (flag != 2) { ten.await(); }
			flag = 3;
			System.out.println(10);
			fif.signal();
		} catch (InterruptedException e){
			flag = 3;
		} finally {
			lock.unlock();
		}
	}
	
	private void printFif() {
		lock.lock();
		try {
			while (flag != 3) { fif.await(); }
			flag = 1;
			System.out.println(15);
			five.signal();
		} catch (InterruptedException e) {
			flag = 1;
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		ConditionD print = new ConditionD();
		// 也可以循环打印，顺序始终不会乱
		new Thread(() -> print.printFif(), "T1").start();
		new Thread(() -> print.printTen(), "T2").start();
		new Thread(() -> print.printFive(), "T3").start();
	}
}
