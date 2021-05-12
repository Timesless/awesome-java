package com.yangzl.interview2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author yangzl
 * @date 2021/3/20
 * 		实现一个自旋锁
 */
@Slf4j
public class MySpinLock {

	AtomicReference<Thread> context = new AtomicReference<>();

	/**
	 * 不释放 cpu 一直轮询，自己是否能加锁成功
	 * 可以考虑每次获取之后都休眠一小段时间（时间可根据业务处理时间来设置）：TimeUnit.MILLISECONDS.sleep(10)
	 */
	public void lock() {
		Thread cur = Thread.currentThread();
		log.info("{} come in", cur.getName());
		while (!context.compareAndSet(null, cur)) {
			;
		}
	}

	public void unLock() {
		Thread cur = Thread.currentThread();
		context.compareAndSet(cur, null);
		log.info("{} come out", cur.getName());
	}

	public void logic(MySpinLock lock) {
		try {
			lock.lock();
			log.info("{} is doing logic...", Thread.currentThread().getName());
			try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
		} finally {
			lock.unLock();
		}
	}

	@Test
	public void testMySpinLock() {
		MySpinLock lock = new MySpinLock();
		new Thread(() -> logic(lock), "T A").start();
		try { TimeUnit.MILLISECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
		new Thread(() -> logic(lock), "T B").start();

		try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
	}
}
