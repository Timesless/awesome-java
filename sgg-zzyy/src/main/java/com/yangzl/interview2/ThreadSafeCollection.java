package com.yangzl.interview2;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author yangzl
 * @date 2021/3/19
 * 		线程安全集合
 * 			CopyOnWriteArrayList
 * 			CopyOnWriteArraySet
 * 			ConcurrentHashMap
 * 			ConcurrentSkipListSet
 * 			ConcurrentSkipListMap
 * 			ConcurrentLinkedQueue
 * 			ConcurrentLinkeDeque
 *
 * 	1. Vector
 * 	2. Collections.synchronized(new ArrayList<>())
 * 	3. java.util.concurrent
 *
 */
public class ThreadSafeCollection {

	@Test
	public void testCopyOnWriteArrayList() {
		CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
	}

}
