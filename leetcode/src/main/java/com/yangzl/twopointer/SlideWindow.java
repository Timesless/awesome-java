package com.yangzl.twopointer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @Author: yangzl
 * @Date: 2020/3/6 14:29
 * @Desc: .. 双指针 => 滑动窗口
 */
public class SlideWindow {

	// =================================================================================
	// 输入一个正整数 target ，输出所有和为 target 的连续正整数序列（至少含有两个数）。
	// 序列内的数字由小到大排列，不同序列按照首个数字从小到大排列。
	// 输入：target = 15 输出：[[1,2,3,4,5],[4,5,6],[7,8]]	1 <= target <= 10^5
	// =================================================================================
	public int[][] findContinuousSequence(int target) {
		int[] sum = new int[target + 1];
		for (int i = 1; i <= target; ++i)
			sum[i] = sum[i - 1] + i;
		List<int[]> rs = new ArrayList<>();
		int l = 0, r = 1;
		int half = (target >>> 1) + 1;
		while (l < r && r <= half) {
			int cur = sum[r] - sum[l];
			if (cur == target) {
				if (r - l > 1) {
					int[] tmp = IntStream.rangeClosed(l + 1, r).toArray();
					rs.add(tmp);
				}
				// 这里可以移动2格，当 2 + 3 + 4 = 9时, 3 + 4 或 3 + 4 + 5 不可能 = 9
				l += 2;
				r++;
			} else if (cur < target) {
				r++;
			} else {
				l++;
			}
		}
		int sz = rs.size();
		int[][] result = new int[sz][];
		for (int i = 0; i < sz; ++i)
			result[i] = rs.get(i);
		return result;
	}
	@Test
	public void testfindContinuousSequence() {
		int[][] rs = findContinuousSequence(15);
		for (int[] r : rs) {
			System.out.print(Arrays.toString(r));
			System.out.println();
		}
	}

	// =================================================================================
	// 合并有序数组，A数组中有足够空间容纳B数组所有数
	// TODO 从A[]尾部放置大的数
	// =================================================================================
	public void merge(int[] A, int m, int[] B, int n) {
		int[] sorted = new int[A.length];
		int i, j, count;
		i = j = count = 0;
		while (i < m && j < n) {
			if (A[i] <= B[j])
				sorted[count++] = A[i++];
			else
				sorted[count++] = B[j++];
		}
		System.out.println(Arrays.toString(sorted));
	}
	@Test
	public void testMerge() {
		int[] m = {1, 2, 3, 0, 0, 0};
		int[] n = {2, 5, 6};
		merge(m, 3, n, 3);
	}
	
	
	
	/**
	 * 2020/10/15 字节春招
	 * 
	 * @param 
	 * @return 
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String[] ND = scanner.nextLine().split(" ");
		int N = Integer.parseInt(ND[0]), D = Integer.parseInt(ND[1]);
		int[] nums = Stream.of(scanner.nextLine().split(" "))
				.limit(N).mapToInt(Integer::parseInt).toArray();

		int i = 0, j = 2;
		long rs = 0;
		while (j < nums.length) {
			while (j < nums.length - 1) {
				if (nums[j + 1] - nums[i] > D){
					break;
				}
				++j;
			}
			if (nums[j] - nums[i] <= D) {
				int count = j - i + 1;
				long tmp = factorial(count) / 6;
				rs += tmp;
			}
			++i;
			++j;
		}
		System.out.println(rs);
	}
	
	static long factorial(int n) {
		long rs = 1;
		while (n > 1) {
			rs *= n;
			--n;
		}
		return rs % 99997867;
	}

}
