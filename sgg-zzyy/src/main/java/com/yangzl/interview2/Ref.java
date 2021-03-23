package com.yangzl.interview2;

import org.junit.jupiter.api.Test;

import java.lang.ref.*;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author yangzl
 * @date 2021/3/20
 * @desc 引用
 * 		Reference 强引用
 * 		SoftReference 软引用
 * 		WeakReference 弱引用
 * 		PhantomReference 虚引用
 * 		ReferenceQueue 引用队列「gc 时 软引用、弱引用、虚引用引用的对象都会被 offer 到与自己关联的引用队列中，可以拿到对象继续操作」
 *
 * 	软引用 / 弱引用的应用场景
 * 	注意使用匿名对象
 *
 */
public class Ref {

	/**
	 * 缓存可能会使用到软引用
	 * 	1. 内存足够，软引用引用的对象的不会被回收
	 * 	2. 内存不足时，gc 时会回收软引用引用的对象
	 *	3. -XX:SoftRefLRUPolicyMSPerMB=50 * -Xms 事件之后 soft引用的对象被回收
	 *
	 * 	-Xms10m -Xmx10m -Xmn4m
	 */
	@Test
	public void testSoftReference() {
		SoftReference<Object> ref = new SoftReference<>(new Object());
		System.out.println(ref.get());
		System.gc();
		System.out.println(ref.get());
		try {
			byte[] bytes = new byte[4 * 1024 * 1024];
		} finally {
			System.out.println(ref.get());
		}
	}

	/**
	 * 软引用引用的对象在 gc 时总会被回收
	 */
	@Test
	public void testWeakReference() {
		WeakReference<Object> ref = new WeakReference<>(new Object());
		System.out.println(ref.get());
		System.gc();
		System.out.println(ref.get());
	}

	@Test
	public void testWeakHashMap() {
		WeakHashMap<String, String> weakMap = new WeakHashMap<>();
		weakMap.put(new String("k"), "weakHashMap");
		System.out.println(weakMap);
		System.gc();
		System.out.println(weakMap);

	}

	@Test
	public void testWeakReferenceQueue() {
		ReferenceQueue<String> q = new ReferenceQueue<>();
		WeakReference<String> ref = new WeakReference<>(new String("zzzz"), q);
		System.out.println(ref.get());
		System.out.println(q.poll());
		System.out.println("========== after gc =============");
		System.gc();
		// SoftRefLRUPolicyMSPerMB * Xmx 剩余空间
		try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
		System.out.println(ref.get());
		Reference<? extends String> refGabage = q.poll();
		System.out.println(refGabage);

		// TODO 在引用队列中拿到之后能做什么呢？
		System.out.println(refGabage.get());
	}

	/**
	 * 虚引用主要作用就是跟踪垃圾回收的状态，确保对象被 finalize 之后，做某些事情
	 *
	 * 1. get 总是返回 null
	 * 2. 总是需要配合 ReferenceQueue 使用
	 */
	@Test
	public void testPhantomReference() {
		ReferenceQueue<String> queue = new ReferenceQueue<>();
		PhantomReference<String> ref = new PhantomReference<>(new String("zzz"), queue);
		System.out.println(ref.get());
		System.out.println(queue.poll());
		System.out.println("========== after gc ============");
		System.gc();
		System.out.println(ref.get());
		System.out.println(queue.poll());
	}
}
