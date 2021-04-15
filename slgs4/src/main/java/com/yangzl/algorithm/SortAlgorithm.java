package com.yangzl.algorithm;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yangzl
 * @date 2019/12/7 23:12
 * @desc .. 数据结构与算法分析描述的几种排序，需掌握
 */
public class SortAlgorithm {

	int[] unsort = {1, 9, 1006, -1562, 4, 102, 3, 88};
	
	/**
	 * 2020/11/26 基数排序， 空间复杂度： 10 * O(n)，时间复杂度 maxLength * O(n)
	 * 			可以考虑用List代替数组存放索引
	 * 要解决负数排序的话，只需要偏移10即可
	 *
	 * @param nums source
	 */
	public static void radixSort(int[] nums) {
		// getMaxLength
		int loops = getDiget(nums), ln = nums.length, bukets = 20;
		int[][] tmp = new int[bukets][ln];
		for (int i = 0, base = 1; i < loops; ++i, base *= 10) {
			// 标识每个桶下一个存放元素的索引
			int[] curIdx = new int[bukets];
			for (int j = 0; j < ln; ++j) {
				int n = nums[j] / base;
				int buket = n % 10, actualBuket = buket + 10;
				tmp[actualBuket][curIdx[actualBuket]++] = nums[j];
			}
			int idx = 0;
			for (int m = 0; m < bukets; ++m) {
				for (int n = 0; n < ln; ++n) {
					if (tmp[m][n] != 0) {
						nums[idx++] = tmp[m][n];
						tmp[m][n] = 0;
					}
				}
			}
		}
	}
	/**
	 * 2020/12/2 基数排序，实现负数的排序
	 * 
	 * @param nums source
	 */
	public static void radixSort2(int[] nums) {
		int digit = getDiget(nums), ln = nums.length;
		// 空间复杂度 20 * ln => O(n)
		List<List<Integer>> list = new ArrayList<>(20);
		for (int i = 0; i < 20; ++i) {
			list.add(new ArrayList<>(ln));
		}
		int[] arr = nums;
		int offset = 10, mod = 10;
		for (int i = 0, base = 1; i < digit; ++i) {
			for (int tmp : arr) {
				int md = (tmp / base) % mod;
				list.get(md + offset).add(tmp);
			}
			// 每一轮排序后的数组
			arr = list.stream().flatMap(List::stream).filter(Objects::nonNull).mapToInt(Integer::intValue).toArray();
			for (List<Integer> cur : list) {
				cur.clear();
			}
			base *= 10;
		}
		// 复制回原数组
		System.arraycopy(arr, 0, nums, 0, ln);
	}
	/**
	 * 2020/12/2 计算数组类最大数值的位数，比如 1358 返回 4，999 返回 3
	 * 
	 * @param nums source
	 * @return int 数值的位数
	 */
	private static int getDiget(int[] nums) {
		int max = 0x80000000, digital = 0;
		for (int i : nums) {
			// 负数
			if (i < 0) {
				i = ~i + 1;
			}
			max = Math.max(max, i);
		}
		while (max > 0) {
			max /= 10;
			++digital;
		}
		return digital;
	}
	@Test
	public void testRadixSort() {
		radixSort(unsort);
		int[] unsorts = {1, 9, 1006, -1562, 4, 102, 3, 88};
		radixSort2(unsorts);
		System.out.println(Arrays.toString(unsorts));
	}
	
	
	/**
	 * 2020/12/2 快排 快速排序实现
	 * 		快排的优化：
	 * 			随机化快排	
	 * 
	 * @param nums source
	 */
	public static void quickSort(int[] nums) {
		quickSort(nums, 0, nums.length - 1);
	}
	private static void quickSort(int[] nums, int start, int end) {
		int l = start, r = end;
		if (l > r) {
			return;
		}
		/*
		 * partion 分区
		 * 选择一个基准值「随机化快排需随机选择一个值」
		 */
		int pivot = nums[l];
		while (l < r) {
			// 右指针向左找到一个小于pivot的值停下
			while (l < r && nums[r] >= pivot) {
				-- r;
			}
			// 因为保存了pivot，
			nums[l] = nums[r];
			// 左指针向右找到一个大于pivot的值停下
			while (l < r && nums[l] <= pivot) {
				++ l;
			}
			nums[r] = nums[l];
		}
		// 填入基准值
		nums[l] = pivot;
		quickSort(nums, start, l - 1);
		quickSort(nums, l + 1, end);
	}

	/**
	 * @date 2020/3/6
	 * @desc 非正式快排实现
	 */
	public static void quickSort(List<Integer> list) {
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
				else if (tmp == pivot) {
					same.add(tmp);
				} else {
					larger.add(tmp);
				}
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
		quickSort(unsort);
		System.out.println(Arrays.toString(unsort));
	}

	/**
	 * @date 2020/3/31
	 * @desc 归并排序
	 * 		主要分为两大步骤：
	 * 			1. 递归分解
	 * 			2. merge
	 */
	public static void mergeSort(int[] nums) {
		mergeSort(nums, 0, nums.length - 1);
	}

	public static void mergeSort(int[] nums, int left, int right) {
		if (left == right) return;
		int mid = left + (right - left >>> 1);
		mergeSort(nums, left, mid);
		mergeSort(nums, mid + 1, right);
		// 合并
		merge(nums, left, mid, right);
	}

	/**
	 * 合并
	 * @param nums 要合并的数组
	 * @param L 左指针
	 * @param M 中间指针
	 * @param R 右指针
	 */
	private static void merge(int[] nums, int L, int M, int R) {
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
		mergeSort(unsort);
		System.out.println(Arrays.toString(unsort));
	}

	/**
	 * @date 2019/12/7 简单插入排序
	 * @desc 掌握
	 */
	public static void insertionSort_1(int[] arr) {
		int x = 1, y, tmp;
		for (; x < arr.length; ++x) {
			tmp = arr[x];
			for (y = x; y > 0 && tmp < arr[y - 1]; --y) {
				arr[y] = arr[y - 1];
			}
			arr[y] = tmp;
		}
	}

	/**
	 * 普通的插入排序，在极端情况下，时间复杂度退化为 O(n2)。比如用例：5 4 3 2 1 时间复杂度
	 *
	 * @param arr arr
	 */
	public static void insertionSort_2(int[] arr) {
		// 第一个元素有序
		int cur, count;
		for (int x = 1, ln = arr.length; x < ln; ++x) {
			cur = arr[x];
			count = 0;
			for (int y = x - 1; y >= 0; --y) {
				if (cur < arr[y]) {
					// 将较大的值往后移一位
					arr[y + 1] = arr[y];
					count++;
				}
			}
			arr[x - count] = cur;
		}
	}

	// _2的改进
	public static void insertionSort_3(int[] arr) {
		for (int x = 1, ln = arr.length; x < ln; ++x) {
			int cur = arr[x];
			int y = x;
			for (; y > 0 && cur < arr[y - 1]; --y)
				arr[y] = arr[y - 1];
			arr[y] = cur;
		}
	}
	@Test
	public void testInsertionSort() {
		insertionSort_1(unsort);
		insertionSort_2(unsort);
		insertionSort_3(unsort);
	}

	/**
	 * 希尔排序「尚硅谷实现」
	 *
	 * @param arr arr
	 */
	public static void shellSort_1(int[] arr) {
		int step = arr.length >> 1;
		int tmp;
		while (step > 0) {
			// 对每组简单插入
			for (int x = step, ln = arr.length; x < ln; ++x) {
				// 这里有点没看懂
				// 2019年12月7日 这里其实就是让y指向每组的第一个元素，和每组后面的元素比较
				for (int y = x - step; y >= 0; y -= step) {
					if (arr[y] > arr[y + step]) {
						tmp = arr[y];
						arr[y] = arr[y + step];
						arr[y + step] = tmp;
					}
				}
			}
			step = step >> 1;
		}
	}

	/**
	 * @date 2019/12/7 希尔排序
	 * @desc 数据结构与算法实现
	 */
	public static void shellSort_2(int[] arr) {
		int step, x, y, tmp, ln;
		for (step = arr.length >>> 1; step > 0; step >>>= 1) {
			// 假设分为3段，那每段的数据都需要自排
			for (x = step, ln = arr.length; x < ln; ++x) {
				tmp = arr[x];
				// 这里与插入排序逻辑相似
				for (y = x; y >= step && tmp < arr[y - step]; y -= step) {
					arr[y] = arr[y - step];
				}
				arr[y] = tmp;
			}
		}
	}
	@Test
	public void testShellSort() {
		shellSort_1(unsort);
		shellSort_2(unsort);
	}

	/**
	 * @date 2020/3/6 冒泡排序
	 * @desc
	 */
	public static void bubbleSort(int[] arr) {
		boolean flag = false;
		int ln = arr.length;
		for (int x = 0; x < ln - 1; ++x) {
			// 每次循环将最大的数移到后面
			for (int y = 0; y < ln - 1 - x; ++y) {
				if (arr[y] > arr[y + 1]) {
					// 交换arr[y] 与 arr[y + 1]
					arr[y] = arr[y] ^ arr[y + 1];
					arr[y + 1] = arr[y] ^ arr[y + 1];
					arr[y] = arr[y] ^ arr[y + 1];
					flag = true;
				}
			}
			// 如果整个内层循环都没有移动元素，则整体已排序完成
			if (!flag) {
				break;
			}
		}
	}
	@Test
	public void testBubbleSort() {
		bubbleSort(unsort);
	}

	/**
	 * @date 2020/3/6
	 * @desc 选择排序
	 */
	public static void selectionSort(int[] arr) {
		int min, minIdx, ln = arr.length - 1;
		// 每次循环只交换一次值，所以比冒泡快
		for (int x = 0; x < ln; ++x) {
			/*
			 * 这个循环是找剩余元素最小值
			 * 首先令每次循环的第一个元素为最小值
			 * 当有比第一个小的值时，记录当前下标
			 */
			min = arr[x];
			minIdx = x;
			// for (int y = x + 1; y < ln && arr[y] < min; ++y)
			for (int y = x + 1; y < ln; ++y)
				if (arr[y] < min) {
					minIdx = y;
				}
			/*
			 * 不等则说明进行了交换
			 * 那么将最小下标的值，赋给每次循环的第一个值
			 * 每次循环的第一个元素值都先保存了，再将第一个元素值赋给最小下标的位置
			 */
			if (minIdx != x) {
				arr[x] = arr[minIdx];
				arr[minIdx] = min;
			}
		}
	}
	@Test
	public void testSelectionSort() {
		selectionSort(unsort);
	}

}
