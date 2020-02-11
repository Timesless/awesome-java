package com.yangzl.algorithm.sort;

import java.util.Arrays;

/**
 * @Author: yangzl
 * @Date: 2019/10/27 18:29
 * @Desc: .. 将数组中，第一个元素当作有序，其余当作无序
 * 				遍历无序表，拿到每个元素将其放在有序表的正确位置
 **/
public class InsertionSort {

	/*
	 * 5 4 3 2
	 * 在极端情况下，需要移动很多次数据
	 */
	static void insertionSort(int[] ary) {
		// 第一个元素有序
		int cur, count;
		for (int x = 1; x < ary.length; ++x) {
			cur = ary[x];
			count = 0;
			for(int y = x - 1; y >= 0; --y) {
				if (cur < ary[y]) {
					// 将较大的值往后移一位
					ary[y + 1] = ary[y];
					count++;
				}
			}
			ary[x - count] = cur;
		}
	}
	/**
	 * @Date: 2020/2/11
	 * @Desc: 实现2
	 **/
	static void insertionSort2(int[] ary) {
		for (int x = 1; x < ary.length; ++x) {
			int cur = ary[x];
			int y = x;
			for (; y > 0 && cur < ary[y - 1]; --y) {
				ary[y] = ary[y - 1];
			}
			ary[y] = cur;
		}
	}

	public static void main(String[] args) {
		int[] ary = {4, 6, 1, 5, 2, 3, -10, 22};
		insertionSort2(ary);
		System.out.println(Arrays.toString(ary));
	}
}
