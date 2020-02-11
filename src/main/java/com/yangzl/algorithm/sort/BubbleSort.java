package com.yangzl.algorithm.sort;

import java.util.Arrays;

/**
 * @Author: yangzl
 * @Date: 2019/10/27 18:29
 * @Desc: .. 需要执行 len - 1次冒泡，每次循环能确定最大值
 * 			这样下次循环就只需排序前len - i个元素
 **/
public class BubbleSort {

	static void bubbleSort(int[] ary) {
		boolean flag = false;
		for(int x = 0; x < ary.length - 1; ++x) {
			// 每次循环将最大的数移到后面
			for (int y = 0; y < ary.length - 1 - x; ++y) {
				if(ary[y] > ary[y+1]) {
					ary[y] = ary[y] ^ ary[y + 1];
					ary[y + 1] = ary[y] ^ ary[y + 1];
					ary[y] = ary[y] ^ ary[y + 1];
					flag = true;
				}
			}
			if(!flag) { break; }
		}
	}


	public static void main(String[] args) {
		int[] ary = {1, 6, 5, 4, 2, 3};
		bubbleSort(ary);
		System.out.println(Arrays.toString(ary));
	}
	
}
