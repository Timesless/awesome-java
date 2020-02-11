package com.yangzl.algorithm.sort;

import java.util.Arrays;

/**
 * @Author: yangzl
 * @Date: 2019/11/10 15:03
 * @Desc: .. 希尔排序： 是简单插入排序的改进版
 * 首先是将元素按步长分组，对每组进行简单插入排序， 因为简单插入对有序的列表基本可以达到现行复杂度
 **/
public class ShellSort {


	static void shellSort(int[] ary) {
		int step = ary.length >> 1;
		int tmp;
		while (step > 0) {
			// 对每组简单插入
			for (int x = step; x < ary.length ; ++x) {
				// 这里有点没看懂
				// 2019年12月7日 这里其实就是让y指向每组的第一个元素，和每组后面的元素比较
				for(int y = x -step; y >= 0; y -= step) {
					if(ary[y] > ary[y + step]) {
						tmp = ary[y];
						ary[y] = ary[y + step];
						ary[y + step] = tmp;
					}
				}
			}
			step = step >> 1;
		}
	}

	public static void main(String[] args) {
		int[] ary = {2, 7, 4, 1, 5, 3, 8, 6, 9, 11};
		shellSort(ary);
		System.out.println(Arrays.toString(ary));
	}
}
