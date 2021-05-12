package com.yangzl.util;

/**
 * @author yangzl
 * @date 2020/11/17 10:06
 *
 * 自实现的一些数学类的工具
 */

public class Maths {
	
	/**
	 * 2020/11/17 辗转取模法 计算最大公约数
	 * 
	 * @param p 参数一
	 * @param  q 参数二
	 * @return long 两数的最大公约数
	 */
	public static long gcd(long p, long q) {
		if (q == 0) return p;
		long r = p % q;
		return gcd(q, r);
	}


	/**
	 * 2020/12/3 统计n - 1以内的质数
	 * 				埃拉托斯特尼筛法
	 *
	 * @param n 大于0的自然数
	 * @return int
	 */
	public static int countPrime(int n) {
		if (n <= 2) {
			return 0;
		}
		int rs = 0;
		boolean[] primes = new boolean[n];
		for (int i = 2; i < n; ++i) {
			if (!primes[i]) {
				++ rs;
				// 划去2的倍数，3的倍数...
				for (int j = 2; i * j < n; ++j) {
					primes[i * j] = true;
				}
			}
		}
		return rs;
	}

	/**
	 * 2020/12/1 左边界的二分查找
	 * 
	 * @param nums source
	 * @param  target target
	 * @return int 目标索引位置
	 */
	public int binarySearchLeft(int[] nums, int target) {
		return binarySearchRight(nums, 0, nums.length - 1, target);
	}
	private int binarySearchLeft(int[] nums, int start, int end, int target) {
		while (start <= end) {
			int mid = start + (end - start >>> 1);
			// 即时nums[mid] == target，也继续望左边找
			if (nums[mid] >= target) {
				return binarySearchLeft(nums, start, mid - 1, target);
			} else {
				return binarySearchLeft(nums, mid + 1, end, target);
			}
		}
		if (start >= 0 && start < nums.length) {
			return nums[start] == target ? start : -1;
		} else {
			return -1;
		}
	}
	
	/**
	 * 2020/12/1 右边界的二分查找
	 * 
	 * @param nums source
	 * @param  target target
	 * @return int 索引位置
	 */
	public int binarySearchRight(int[] nums, int target) {
		return binarySearchRight(nums, 0, nums.length - 1, target);
	}
	private int binarySearchRight(int[] nums, int start, int end, int target) {
		while (start <= end) {
			int mid = start + (end - start >>> 1);
			// 即时nums[mid] == target，也继续往右找
			if (nums[mid] <= target) {
				return binarySearchRight(nums, mid + 1, end, target);
			} else {
				return binarySearchRight(nums, start, mid - 1, target);
			}
		}
		if (end >= 0 && end < nums.length) {
			return nums[end] == target ? end : -1;
		} else {
			return -1;
		}
	}

}
