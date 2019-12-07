package com.yinhai.algorithm.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: yangzl
 * @Date: 2019/12/7 23:12
 * @Desc: .. 数据结构与算法分析描述的几种排序，需掌握
 **/
public class SomeSort {

	/**
	 * @Date: 2019/12/7 简单插入排序
	 * @Desc:
	 **/
	static void insertionSort(int[] arr) {

		int x = 1, y, tmp;
		for (; x < arr.length; ++x) {
			tmp = arr[x];
			for (y = x; y > 0 && tmp < arr[y - 1]; --y) { arr[y] = arr[y - 1]; }
			arr[y] = tmp;
		}
		System.out.println(Arrays.toString(arr));
	}
	
	/**
	 * @Date: 2019/12/7 希尔排序
	 * @Desc: 
	 **/
	static void shellSort(int[] arr) {
		
		int step, x, y, tmp;
		for (step = arr.length >>> 1; step > 0; step = step >>> 1) {
			for (x = step; x < arr.length; ++x) {
				tmp = arr[x];
				for (y = x; y >= step && tmp < arr[y - step]; y -= step) { arr[y] = arr[y - step]; }
				arr[y] = tmp;
			}
		}
		System.out.println(Arrays.toString(arr));
	}
	
	public static void main(String[] args) {
		int[] arr = {1, 9, 6, 5, 4, 2, 3, 8};
		// insertionSort(arr);
		shellSort(arr);
	}
}
