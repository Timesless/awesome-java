package com.leetcode.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

/**
 * @Date: 2020/1/19
 * @Desc: 编写一个可以从 1 到 n 输出代表这个数字的字符串的程序，但是：
 * 如果这个数字可以被 3 整除，输出 "fizz"。
 * 如果这个数字可以被 5 整除，输出 "buzz"。
 * 如果这个数字可以同时被 3 和 5 整除，输出 "fizzbuzz"。
 * 例如，当 n = 15，输出： 1, 2, fizz, 4, buzz, fizz, 7, 8, fizz, buzz, 11, fizz, 13, 14, fizzbuzz。
 * 假设有这么一个类：
 * class FizzBuzz {
 * public FizzBuzz(int n) { ... }               // constructor
 * public void fizz(printFizz) { ... }          // only output "fizz"
 * public void buzz(printBuzz) { ... }          // only output "buzz"
 * public void fizzbuzz(printFizzBuzz) { ... }  // only output "fizzbuzz"
 * public void number(printNumber) { ... }      // only output the numbers
 * }
 * 请你实现一个有四个线程的多线程版  FizzBuzz， 同一个 FizzBuzz 实例会被如下四个线程使用：
 * 线程A将调用 fizz() 来判断是否能被 3 整除，如果可以，则输出 fizz。
 * 线程B将调用 buzz() 来判断是否能被 5 整除，如果可以，则输出 buzz。
 * 线程C将调用 fizzbuzz() 来判断是否同时能被 3 和 5 整除，如果可以，则输出 fizzbuzz。
 * 线程D将调用 number() 来实现输出既不能被 3 整除也不能被 5 整除的数字。
 **/
public class FizzBuzz {
	private int n;
	private Lock lock = new ReentrantLock();
	private Condition c = lock.newCondition();
	private Condition c3 = lock.newCondition();
	private Condition c5 = lock.newCondition();
	private Condition c15 = lock.newCondition();
	private boolean flag = false;

	public FizzBuzz(int n) { this.n = n; }

	public void fizz(Runnable printFizz) throws InterruptedException {
		for (int x = 3; x <= n; x += 3) {
			lock.lock();
			try {
				if (x % 5 != 0) {
					while (!flag) { c3.await(); }
					flag = false;
					printFizz.run();
					c.signal();
				}
			} finally {
				lock.unlock();
			}
		}
	}

	public void buzz(Runnable printBuzz) throws InterruptedException {
		for (int x = 5; x <= n; x += 5) {
			lock.lock();
			try {
				if (x % 3 != 0) {
					while (!flag) { c5.await(); }
					flag = false;
					printBuzz.run();
					c.signal();
				}
			} finally {
				lock.unlock();
			}
		}
	}

	public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
		for (int x = 15; x <= n; x += 15) {
			lock.lock();
			try {
				while (!flag) { c15.await(); }
				flag = false;
				printFizzBuzz.run();
				c.signal();
			} finally {
				lock.unlock();
			}
		}
	}

	public void number(IntConsumer printNumber) throws InterruptedException {
		for (int x = 1; x <= n; ++x) {
			lock.lock();
			try {
				while (flag) { c.await(); }
				flag = true;
				int m1 = x % 3, m2 = x % 5;
				if (m1 == 0 && m2 == 0) {
					c15.signal();
				} else if (m1 == 0) {
					c3.signal();
				} else if (m2 == 0) {
					c5.signal();
				} else {
					flag = false;
					printNumber.accept(x);
					c.signal();
				}
			} finally {
				lock.unlock();
			}
		}
	}

	public static void main(String[] args) {
		FizzBuzz fz = new FizzBuzz(15);
		new Thread(() -> {
			try {
				fz.fizz(() -> System.out.print("fizz "));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		new Thread(() -> {
			try {
				fz.buzz(() -> System.out.print("buzz "));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		new Thread(() -> {
			try {
				fz.fizzbuzz(() -> System.out.print("fizzbuzz "));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
		new Thread(() ->
		{
			try {
				fz.number(n -> System.out.print(n + " "));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
}