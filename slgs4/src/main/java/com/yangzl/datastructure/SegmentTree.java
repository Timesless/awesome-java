package com.yangzl.datastructure;

import java.util.Arrays;
import java.util.function.BinaryOperator;

/**
 * @Author: yangzl
 * @Date: 2020/2/13 12:10
 * @Desc: .. 线段树，区域内检索和更新
 *  用于解决多种范围查询问题，在对数时间内从数组中找到最小值、最大值、总和、最大公约数、最小公倍数等。
 */
public class SegmentTree<E> {
	private final E[] ele;
	// 线段树
	private final E[] tree;
	/*
	 * 这里是否 ? super E
	 */
	private final BinaryOperator<E> operator;
	
	public SegmentTree(E[] arr, BinaryOperator<E> operator) {
		this.operator = operator;
		this.ele = (E[]) new Object[arr.length];
		for (int i = 0; i < arr.length; i++)
			ele[i] = arr[i];
		this.tree = (E[]) new Object[arr.length << 1];
		buildSegmentTree(0, 0, ele.length - 1);
	}
	
	/**
	 * @Date: 2020/2/13
	 * @Desc: 查询区间rl..queryR
	 */
	public E queryRange(int queryL, int queryR) {
		return queryRange(0, 0, ele.length - 1, queryL, queryR);
	}
	/**
	 * @Date: 2020/2/13
	 * @Desc: treeIdx区间树的索引，在数组下标left...right，查询区间rl..rangeR
	 */
	private E queryRange(int treeIdx, int left, int right, int rangeL, int rangeR) {
		if (left == rangeL && right == rangeR) { return tree[treeIdx]; }
		// 查询区间与当前节点中间点比较
		int mid = left + (right - left >>> 1);
		// 左右孩子
		int leftIdx = (treeIdx << 1) + 1;
		int rightIdx = (treeIdx << 1) + 2;
		// 区间在右侧
		if (rangeL > mid)
			return queryRange(rightIdx, rangeL, right, rangeL, rangeR);
		else if (rangeR <= mid)
			return queryRange(leftIdx, left, rangeR, rangeL, rangeR);
		// 区间在分布在左右两侧，需要从左到中间，从中间到右查询后合并
		E lResult = queryRange(leftIdx, left, rangeL, rangeL, mid);
		E rResult = queryRange(rightIdx, rangeL, right, rangeL, rangeR);
		return operator.apply(lResult, rResult);
	}

	@Override
	public String toString() { return Arrays.toString(tree); }

	/**
	 * @Date: 2020/2/13
	 * @Desc: 在treeIdx位置，构建线段树
	 */
	private void buildSegmentTree(int treeIdx, int lt, int rt) {
		if (lt == rt) {
			tree[treeIdx] = ele[lt];
			return;
		}
		int leftIdx = (treeIdx << 1) + 1;
		int rightIdx = (treeIdx << 1) + 2;
		/*
		 * 注意，这里要用数据的l，r
		 * mid = lt + rt >>> 1，这样可能溢出
		 */
		int mid = lt + (rt - lt >>> 1);
		buildSegmentTree(leftIdx, lt, mid);
		buildSegmentTree(rightIdx, mid + 1, rt);
		// 用户定义合并逻辑
		tree[treeIdx] = operator.apply(tree[leftIdx], tree[rightIdx]);
	}


	public static void main(String[] args) {
		Integer[] num = {1, 3, 5, 6, 7};
		BinaryOperator<Integer> operator = Integer::sum;
		SegmentTree<Integer> tree = new SegmentTree<>(num, operator);
		System.out.println(tree.toString());
		System.out.println(tree.queryRange(0, 4));
	}
}
