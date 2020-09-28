package com.yangzl.algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: yangzl
 * @Date: 2019/12/7 23:12
 * @Desc: .. 数据结构与算法分析描述的几种排序，需掌握
 */
public class SortAlgorithm {

	int[] unsort = {1, 9, 6, 5, 4, 2, 3, 8};

	/**
	 * @Date: 2020/3/6
	 * @Desc: 非正式快排实现
	 */
	static void quickSort(List<Integer> list) {
		int sz = list.size();
		if (sz > 1) {
			List<Integer> smaller = new ArrayList<>(sz);
			List<Integer> same = new ArrayList<>(sz);
			List<Integer> larger = new ArrayList<>(sz);
			// 基准值
			int pivot = list.get(0);
			for (Integer tmp : list) {
				if (tmp < pivot)
					smaller.add(tmp);
				else if (tmp == pivot)
					same.add(tmp);
				else
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
	@Test
	public void testQuickSort() {
		List<Integer> list = Stream.of(2, 7, 4, 1, 3, 5, 8, 6, 9, 11).collect(Collectors.toList());
		quickSort(list);
		System.out.println(list);
	}

	/**
	 * @Date: 2020/3/31
	 * @Desc: 归并排序
	 */
	public void mergeSort(int[] nums, int left, int right) {
		if (left == right) return;
		int mid = left + (right - left >>> 1);
		mergeSort(nums, left, mid);
		mergeSort(nums, mid + 1, right);
		// 合并
		merge(nums, left, mid, right);
	}
	public void merge(int[] nums, int L, int M, int R) {
		int[] tmp = new int[R - L + 1];
		// 左右指针，tmp数组指针
		int i = L, j = M + 1, p = 0;
		while (i <= M && j <= R) {
			if (nums[i] <= nums[j])
				tmp[p++] = nums[i++];
			else
				tmp[p++] = nums[j++];
		}
		while (i <= M) tmp[p++] = nums[i++];
		while (j <= R) tmp[p++] = nums[j++];
		/*
		 * 复制回原数组
		 * for (int k = 0; k < tmp.length; ++k)
		 *     nums[L + k] = tmp[k];
		 * optimization System.arraycopy()
		 */
		System.arraycopy(tmp, 0, nums, L, tmp.length);
	}
	@Test
	public void testMergeSort() {
		mergeSort(unsort, 0, unsort.length - 1);
		System.out.println(Arrays.toString(unsort));
	}

	/**
	 * @Date: 2019/12/7 简单插入排序
	 * @Desc: 掌握
	 */
	static void insertionSort_1(int[] arr) {
		int x = 1, y, tmp;
		for (; x < arr.length; ++x) {
			tmp = arr[x];
			for (y = x; y > 0 && tmp < arr[y - 1]; --y) { arr[y] = arr[y - 1]; }
			arr[y] = tmp;
		}
		System.out.println(Arrays.toString(arr));
	}
	// 在极端情况下，需要移动很多次数据 5 4 3 2
	static void insertionSort_2(int[] arr) {
		// 第一个元素有序
		int cur, count;
		for (int x = 1; x < arr.length; ++x) {
			cur = arr[x];
			count = 0;
			for(int y = x - 1; y >= 0; --y) {
				if (cur < arr[y]) {
					// 将较大的值往后移一位
					arr[y + 1] = arr[y];
					count++;
				}
			}
			arr[x - count] = cur;
		}
		System.out.println(Arrays.toString(arr));
	}
	// _2的改进
	static void insertionSort_3(int[] arr) {
		for (int x = 1; x < arr.length; ++x) {
			int cur = arr[x];
			int y = x;
			for (; y > 0 && cur < arr[y - 1]; --y) 
				arr[y] = arr[y - 1];
			arr[y] = cur;
		}
		System.out.println(Arrays.toString(arr));
	}
	@Test
	public void testInsertionSort() {
		insertionSort_1(unsort);
		insertionSort_2(unsort);
		insertionSort_3(unsort);
	}
	
	// 尚硅谷实现
	static void shellSort_1(int[] arr) {
		int step = arr.length >> 1;
		int tmp;
		while (step > 0) {
			// 对每组简单插入
			for (int x = step; x < arr.length ; ++x) {
				// 这里有点没看懂
				// 2019年12月7日 这里其实就是让y指向每组的第一个元素，和每组后面的元素比较
				for(int y = x -step; y >= 0; y -= step) {
					if(arr[y] > arr[y + step]) {
						tmp = arr[y];
						arr[y] = arr[y + step];
						arr[y + step] = tmp;
					}
				}
			}
			step = step >> 1;
		}
		System.out.println(Arrays.toString(arr));
	}
	/**
	 * @Date: 2019/12/7 希尔排序
	 * @Desc:  数据结构与算法实现
	 */
	static void shellSort_2(int[] arr) {
		int step, x, y, tmp;
		for (step = arr.length >>> 1; step > 0; step = step >>> 1) {
			// 假设分为3段，那每段的数据都需要自排
			for (x = step; x < arr.length; ++x) {
				tmp = arr[x];
				// 这里与插入排序逻辑相似
				for (y = x; y >= step && tmp < arr[y - step]; y -= step) { arr[y] = arr[y - step]; }
				arr[y] = tmp;
			}
		}
		System.out.println(Arrays.toString(arr));
	}
	@Test
	public void testShellSort() {
		shellSort_1(unsort);
		shellSort_2(unsort);
	}
	
	/**
	 * @Date: 2020/3/6 冒泡排序
	 * @Desc: 
	 */
	static void bubbleSort(int[] arr) {
		boolean flag = false;
		for(int x = 0; x < arr.length - 1; ++x) {
			// 每次循环将最大的数移到后面
			for (int y = 0; y < arr.length - 1 - x; ++y) {
				if(arr[y] > arr[y+1]) {
					arr[y] = arr[y] ^ arr[y + 1];
					arr[y + 1] = arr[y] ^ arr[y + 1];
					arr[y] = arr[y] ^ arr[y + 1];
					flag = true;
				}
			}
			if(!flag) { break; }
		}
		System.out.println(Arrays.toString(arr));
	}
	@Test
	public void testBubbleSort() { bubbleSort(unsort); }
	
	/**
	 * @Date: 2020/3/6
	 * @Desc:  选择排序
	 */
	static void selectionSort(int[] arr) {
		int min, minIdx;
		for (int x = 0; x < arr.length - 1; ++x) {	// 每次循环只交换一次值，所以比冒泡快
			/*
			 * 这个循环是找剩余元素最小值
			 * 首先令每次循环的第一个元素为最小值
			 * 当有比第一个小的值时，记录当前下标
			 */
			min = arr[x];
			minIdx = x;
			// for (int y = x + 1; y < arr.length && arr[y] < min; ++y)
			for (int y = x + 1; y < arr.length; ++y)
				if(arr[y] < min) { minIdx = y; }
			/*
			 * 不等则说明进行了交换
			 * 那么将最小下标的值，赋给每次循环的第一个值
			 * 每次循环的第一个元素值都先保存了，再将第一个元素值赋给最小下标的位置
			 */
			if(minIdx != x) {
				arr[x] = arr[minIdx];
				arr[minIdx] = min;
			}
		}
		System.out.println(Arrays.toString(arr));
	}
	@Test
	public void testSelectionSort() { selectionSort(unsort); }
	
}
