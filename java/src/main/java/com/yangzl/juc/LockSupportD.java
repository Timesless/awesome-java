package com.yangzl.juc;

import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author: yangzl
 * @Date: 2020/8/28 09:59
 * @Desc: .. Java并发编程之美 > 并发包锁原理 > LockSupport工具类
 */
public class LockSupportD {
	
	/**
	 * 1.获取UnSafe实例，不创建实例
	 * 2.反射构造器，newInstance创建Unsafe实例
	 */
	private static Unsafe UNSAFE;
	static {
		try {
			Field unsafe = Unsafe.class.getDeclaredField("theUnsafe");
			unsafe.setAccessible(true);
			// 从哪个对象获取unsafe实例
			UNSAFE = (Unsafe) unsafe.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Date: 2020/8/28
	 * @Desc: 测试其它线程调用 因park()被阻塞线程的interrupt()不会InterruptedException
	 * 其他线程调用了阻塞线程的interrupt()，设置了中断标志或者线程被虚假唤醒，则阻塞线程也会返回
	 */
	@Test
	public void testOtherThreadUnpark() {
		// 这个线程无LockSupport许可证，RUNNABLE后直接阻塞
		Thread t1 = new Thread(LockSupport::park);
		t1.start();

		// 在main线程，中断了t1线程，无InterruptedException
		t1.interrupt();
	}
	

}
