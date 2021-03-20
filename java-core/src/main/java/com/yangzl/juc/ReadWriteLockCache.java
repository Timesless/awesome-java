package com.yangzl.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author yangzl
 * @date 2020/1/5 00:06
 * @desc Interface ReadWriteLock
 * 		实现一个线程安全的读写分离缓存
 *
 * 	ReentrantReadWriteLock 高16位表示读锁，低16位表示写锁
 *		ReentrantReadWriteLock.readLock();
 *		ReentrantReadWriteLock.writeLock();
 */
@Slf4j
public class ReadWriteLockCache {

	private final Map<String, Object> map = new HashMap<>(256);
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	Lock writeLock = this.lock.writeLock();
	Lock readLock = this.lock.readLock();

	/**
	 * 向缓存中放入数据
	 *
	 * @param k k
	 * @param v v
	 */
	private void put(String k, Object v) {
		writeLock.lock();
		try {
			log.info("{} 写数据 ", Thread.currentThread().getName());
			map.put(k, v);
			try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
			log.info("{} 写数据完成 ", Thread.currentThread().getName());
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * 从缓存中获取当前线程保存的数据
	 *
	 * @param k k
	 * @return obj
	 */
	private Object get(String k) {
		Object rs = null;
		readLock.lock();
		String threadName = Thread.currentThread().getName();
		try {
			log.info("{} 读取数据", threadName);
			rs = map.get(k);
			log.info("{} 读数据完成", threadName);
		} finally {
			readLock.unlock();
		}
		return rs;
	}

	/**
	 * 运行结果得到：读读共享，写写 / 读写互斥
	 * @param args args
	 */
	public static void main(String[] args) {
		ReadWriteLockCache cache = new ReadWriteLockCache();
		/*IntStream.rangeClosed(1, 4).forEach(
				x -> {
					String sx = String.valueOf(x);
					new Thread(() -> cache.put(sx, x)).start();
					new Thread(() -> cache.get(sx)).start();
					new Thread(() -> cache.get(sx)).start();
					new Thread(() -> cache.get(sx)).start();
				}
		)*/
		// max = 16 + 20，之后将触发 ThreadPoolExecutor.AbortPolicy
		BlockingQueue<Runnable> q = new ArrayBlockingQueue<>(20);
		AtomicInteger threadPrefix = new AtomicInteger();
		ThreadFactory factory = r -> new Thread(r, String.valueOf(threadPrefix.getAndIncrement()));
		ThreadPoolExecutor.AbortPolicy policy = new ThreadPoolExecutor.AbortPolicy();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(16, 16, 60, TimeUnit.SECONDS, q, factory, policy);

		// 3 写 9 读
		for (int i = 0; i < 3; i++) {
			final int fi = i;
			String is = String.valueOf(i);
			// execute 无返回值， submit 有返回值
			executor.execute(() -> cache.put(is, fi));
			executor.execute(() -> cache.get(is));
			executor.execute(() -> cache.get(is));
			executor.execute(() -> cache.get(is));
		}
		executor.shutdown();
	}
}
