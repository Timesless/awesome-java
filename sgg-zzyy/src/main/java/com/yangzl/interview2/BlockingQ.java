package com.yangzl.interview2;

import org.junit.jupiter.api.Test;

/**
 * @author yangzl
 * @date 2021/3/20
 *
 * interface BlockingQueue「阻塞队列」
 *								消息中间件的核心
 * 		阻塞 API：
 * 			1. put / take
 * 			2. offer(long, TimeUnit) / poll(long, TimeUnit)
 * 	具体类：
 * 		LinkedBlockingQueue「无界阻塞队列，或者说界限为 Integer.MAX_VALUE」
 * 		LinkedBlockingDeque「无界阻塞双端队列」
 * 		LinkedTransferQueue「无界阻塞队列，不仅仅是阻塞在添加元素，而是阻塞到自己添加的元素被某一个线程消费」
 * 		ArrayBlockingQueue「有界阻塞队列，数组实现，无法扩容」
 * 		SynchronousQueue「不保存元素的阻塞队列」
 * 		PriorityBlockingQueue「带优先级的阻塞队列，数组实现，可以扩容 MAX_ARRAY_SIZE = 0x7fffffff - 8」
 * 		DelayQueue「无界带优先级的延迟阻塞队列」
 *
 */
public class BlockingQ {

	@Test
	public void testLinkedBlockingQeque() {

	}

	@Test
	public void testLinkedTransferQueue() {

	}

	@Test
	public void testLinkedBlockingDeque() {

	}

	@Test
	public void testArrayBlockingQeque() {

	}

	@Test
	public void testPriorityBlockingQueue() {

	}

	/**
	 * 奢侈品不下单不开工
	 * 容量为 0，每个插入必须等待另一个线程删除，反之亦然
	 */
	@Test
	public void testSynchronousQueue() {

	}

	@Test
	public void testDelayQueue() {

	}

}
