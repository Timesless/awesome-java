package com.yangzl.algorithm.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yangzl
 * @Date: 2019/10/29 20:42
 * @Desc: .. 快排： 选取数组中第一个 | 其他元素为基准值，将数据分区小于该值的在左边，大于该值的在右边，
 * 					递归的执行分区直到不能再分
 **/
public class QuickSort {

	/**
	 * @Date: 2019/12/7 非正式快排
	 * @Desc:
	 **/
	static void quickSort(List<Integer> list) {
		int sz = list.size();
		if (sz > 1) {
			List<Integer> smaller = new ArrayList<>(sz);
			List<Integer> same = new ArrayList<>(sz);
			List<Integer> larger = new ArrayList<>(sz);
			// 基准值
			int pivot = list.get(0);
			for (Integer tmp : list) {
				if (tmp < pivot) {
					smaller.add(tmp);
				} else if (tmp == pivot) {
					same.add(tmp);
				} else
					larger.add(tmp);
			}
			quickSort(smaller);
			quickSort(larger);

			list.clear();
			list.addAll(smaller);
			list.addAll(same);
			list.addAll(larger);
		}
	}

	public static void main(String[] args) {
		int[] arr = {2, 7, 4, 1, 3, 5, 8, 6, 9, 11};
		List<Integer> list = new ArrayList<>(arr.length);
		for (int tmp : arr) { list.add(tmp); }
		quickSort(list);
		System.out.println(list);
	}
}
