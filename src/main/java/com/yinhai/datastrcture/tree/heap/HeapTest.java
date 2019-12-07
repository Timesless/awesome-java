package com.yinhai.datastrcture.tree.heap;

import org.junit.Test;

/**
 * @Author: yangzl
 * @Date: 2019/12/7 22:43
 * @Desc: ..
 **/
public class HeapTest {

	@Test
	public void testHeapSort() {
		int[] arr = {4, 6, 8, 5, 9, -1, 99};
		BinaryHeap heap = new BinaryHeap(arr);
		heap.printHeap();
		System.out.println();
		heap.heapSort();
	}
	
}
