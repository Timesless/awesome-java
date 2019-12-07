package com.yinhai.algorithm.sort;

import java.util.Arrays;

/**
 * @Author: yangzl
 * @Date: 2019/10/27 18:30
 * @Desc: .. 每次从剩下的数中选择一个最小的依次放入到索引为0，1，2...idx - 1
 **/
public class SelectionSort {

	// 5 1 3
	static void selectionSort(int[] ary) {
		int min, minIdx;
		for (int x = 0; x < ary.length - 1; ++x) {	// 每次循环只交换一次值，所以比冒泡快
			/*
			 * 这个循环是找剩余元素最小值
			 * 首先令每次循环的第一个元素为最小值
			 * 当有比第一个小的值时，记录当前下标
			 */
			min = ary[x];
			minIdx = x;
			for (int y = x + 1; y < ary.length; ++y) {
				if(ary[y] < min) { minIdx = y; }
			}
			/*
			 * 不等则说明进行了交换
			 * 那么将最小下标的值，赋给每次循环的第一个值
			 * 每次循环的第一个元素值都先保存了，再将第一个元素值赋给最小下标的位置
			 */
			if(minIdx != x) {
				ary[x] = ary[minIdx];
				ary[minIdx] = min;
			}
		}
	}

	public static void main(String[] args) {
		int[] ary = {1, 4, 5, 3, 2, 6};
		selectionSort(ary);
		System.out.println(Arrays.toString(ary));
	}
}
