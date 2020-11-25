package com.yangzl.datastructure;

import java.util.Arrays;

/**
 * @Author: yangzl
 * @Date: 2020/2/12 12:47
 * @Desc: .. 二叉最小堆实现优先最小队列，出队O1，入队最差OlogN；可以实现排序
 * 	标准库中PriorityQueue也是最小堆实现
 * 	实现1000000元素，选出最小的100个元素
 * 	实现d堆
 * 	实现索引堆
 */
public class PriorityHeap<E extends Comparable<? super E>> {

	private int size;
	// 完全二叉树以数组实现
	private E[] heap;
	// private final Comparator<? super E> comparator;
	public PriorityHeap() { this(10); }

	/*
	 * 	2020年6月7日
	 * 	Doug Lea的源码，都使用Object[]，用到元素时再执行强转
	 */
	public PriorityHeap(int capacity) { this.heap = (E[]) new Comparable[capacity]; }
	
	public int size() { return size; }
	public boolean isEmpty() { return size == 0; }
	// 根元素
	public E peek() {
		if (isEmpty()) throw new IllegalArgumentException("空队列");
		return heap[0];
	}
	
	/**
	 * @Date: 2020/2/12
	 * @Desc: 入队
	 */
	public void offer(E e) {
		if (size == heap.length) 
			growUp(size + (size >>> 1));
		heap[size++] = e;
		percolateUp(size - 1);
	}
	
	// 出队
	public E poll() {
		if (isEmpty()) throw new IllegalArgumentException("空队列");
		E e = heap[0];
		heap[0] = heap[size - 1];
		percolateDown(0);
		heap[--size] = null;
		return e;
	}
	
	// ====================================================================
	// divide line
	// ====================================================================
	
	/**
	 * @Date: 2020/2/12
	 * @Desc: 上滤
	 */
	private void percolateUp(int hole) {
		E e = heap[hole];
		while (hole > 0) {
			int p = (hole - 1) >>> 1;
			if (e.compareTo(heap[p]) < 0) {
				heap[hole] = heap[p];
				hole = p;
			} else 
				break;
		}
		heap[hole] = e;
	}
	
	/**
	 * @Date: 2020/2/12
	 * @Desc: 下滤
	 */
	private void percolateDown(int hole) {
		E e = heap[hole];
		while (hole < (size >>> 1)) {
			int s = (hole << 1) + 1;
			int r = s + 1;
			// 右子节点比左子节点小，那么取右子节点（最小堆）
			if (r < size && heap[r].compareTo(heap[s]) < 0) {
				s = r;
			}
			if (e.compareTo(heap[s]) > 0) {
				heap[hole] = heap[s];
				hole = s;
			} else 
				break;
		}
		heap[hole] = e;
	}

	private void growUp(int newCapacity) {
		// 边界检查newCapacity
		heap = Arrays.copyOf(heap, newCapacity);
	}
	
	/**
	 * @Date: 2020/2/12
	 * @Desc: 获取parent,left,right索引的封装
	 */
	private int parent(int idx) { return idx - 1 >>> 1; }
	private int left(int idx) { return (idx << 1) + 1; }
	private int right(int idx) { return (idx << 1) + 2; }
	// 边界检查
	private void rangeCheck(int idx) { }

	public static void main(String[] args) {
		PriorityHeap<Integer> heap = new PriorityHeap<Integer>(5);
		heap.offer(8);
		heap.offer(6);
		heap.offer(4);
		heap.offer(5);
		heap.offer(7);
		System.out.println(heap.poll());
		System.out.println(heap.poll());
		System.out.println(heap.poll());
		System.out.println(heap.poll());
		System.out.println(heap.poll());
	}
}
