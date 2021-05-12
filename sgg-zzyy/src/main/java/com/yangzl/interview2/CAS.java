package com.yangzl.interview2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yangzl
 * @date 2021/3/19
 *
 * 		CAS ==> compare and swap
 * 		「OS 提供的指令」
 * 			1. COMPARE AND SWAP
 * 			2. TEST AND SWAP
 * 			3. ...
 * 		原语，不可中断
 * 		不可中断通过「关中断」实现
 * 	缺点：
 * 		自旋时不放弃 cpu
 * 		只能保证一个共享变量的原子性
 * 		ABA 问题？
 * 			根据周志明老师说法，ABA 问题很鸡肋，
 * 				1. 很多情况下 ABA 并不会影响程序的正确性
 * 				2. 如果影响程序正常执行，那完全可以使用用 synchronized / ReentrantLock 加锁解决
 *
 * 	ABA 解决：
 * 		原子引用更新「AtomicReference, AtomicFieldUpdater」
 */
@Slf4j
public class CAS {

	/** 初始化 unsafe 实例 */
	private static Unsafe unsafe;
	static {
		try {
			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			unsafe = (Unsafe) theUnsafe.get(null);
		} catch (Exception e) {
			System.out.println(" unsafe 获取异常 ");
		}
	}

	private volatile int v;

	/**
	 * return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
	 */
	@Test
	public void testAtomicInteger() {
		AtomicInteger atomic = new AtomicInteger(2);
		atomic.compareAndSet(2, 5);
		log.info("current data = {}", atomic.get());
	}

	/**
	 * unsafe 操作对象字段值
	 */
	@Test
	public void testUnsafeOpFieldVal() {
		try {
			// 获取 v 内存偏移量
			long vOffset = unsafe.objectFieldOffset(CAS.class.getDeclaredField("v"));
			unsafe.compareAndSwapInt(this, vOffset, 0, 1);
			log.info("this.v = {}", this.v);
		} catch (Throwable t) {
			log.error(t.getMessage());
		}
	}

	/**
	 * 多线程环境下 unsafe 操作对象的字段值
	 */
	@Test
	public void testMTUnsafeOpFieldVal() {
		try {
			long offset = unsafe.objectFieldOffset(CAS.class.getDeclaredField("v"));
			int cv;
			do {
				cv = unsafe.getIntVolatile(this, offset);
			} while (!unsafe.compareAndSwapInt(this, offset, cv, cv + 1));
		} catch (Throwable t) {
			log.error(t.getMessage());
		}
		log.info("this.v = {}", this.v);
	}


	// ==================================================================
	// ==================================================================

	/**
	 * ABA
	 * 	T1 耗时 10s
	 * 	T2 耗时 2s
	 * 	当 T1 执行时，T2已经改过很多次数据（ABBCA），但又改回去 T1 期望的值了
	 */
	@Test
	public void testABA() {
		AtomicInteger atomic = new AtomicInteger();
		new Thread(() -> {
			atomic.compareAndSet(0, 100);
			atomic.compareAndSet(100, 0);
		}, "T A").start();
		try { TimeUnit.MILLISECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
		new Thread(() -> {
			atomic.compareAndSet(0, 99);
		}).start();
		try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

		log.info("after ABA, val = {}", atomic.get());
	}

}
