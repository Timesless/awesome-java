package com.yangzl.contest;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author yangzl
 * @date 2020/10/31 22:23
 * @desc
 */
public class Contest38 {

	/**
	 * 2020/11/1 
	 * 5539. 按照频率将数组升序排序 
	 * 给你一个整数数组 nums ，请你将数组按照每个值的频率 升序 排序。如果有多个值的频率相同，请你按照数值本身将它们 降序 排序。 
	 * 请你返回排序后的数组。
	 * 示例 1：
	 * 输入：nums = [1,1,2,2,2,3]
	 * 输出：[3,1,1,2,2,2]
	 * 解释：'3' 频率为 1，'1' 频率为 2，'2' 频率为 3 。
	 * @param nums 参数
	 * @return int[]
	 */
	public int[] frequencySort(int[] nums) {
		Map<Integer, Integer> map = new HashMap<>(nums.length);
		for (int n : nums) {
			map.merge(n, 1, Integer::sum);
		}
		Comparator<Map.Entry<Integer, Integer>> comparator = (o1, o2) -> {
			int v1 = o1.getValue(), v2 = o2.getValue();
			int k1 = o1.getKey(), k2 = o2.getKey();
			return v1 - v2 == 0 ? (k1 - k2 == 0 ? k2 : k2 - k1) : v1 - v2;
		};
		List<Map.Entry<Integer, Integer>> collect = new ArrayList<>(map.entrySet());
		collect.sort(comparator);
		int[] rs = new int[nums.length];
		int index = 0;
		for (Map.Entry<Integer, Integer> entry : collect) {
			int k = entry.getKey(), v = entry.getValue();
			for (int i = 0; i < v; ++i) {
				rs[index++] = k;
			}
		}
		return rs;
	}
	@Test
	public void test() {
		int[] arr = {1,1,1,2,2,2,3};
		System.out.println(Arrays.toString(frequencySort(arr)));
	}
	

	public boolean canFormArray(int[] arr, int[][] pieces) {
		String arrString = Arrays.stream(arr).boxed().map(String::valueOf).collect(Collectors.joining(""));
		for (int[] tmp : pieces) {
			String tmpString = Arrays.stream(tmp).boxed().map(String::valueOf).collect(Collectors.joining(""));
			if (!arrString.contains(tmpString)) {
				return false;
			}
		}
		return true;
	}
	@Test
	public void testCount() {
		int[] arr = {1, 3, 5, 7};
		int[][] pieces = { {2, 4, 6, 8} };
		System.out.println(canFormArray(arr, pieces));
		System.out.println(Arrays.toString(arr));
		
	}
	
	
	@Test
	public void testCompletableFuture() {
		CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
			System.out.println("任务1");
		});

		CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
			System.out.println("任务2");
		}).whenComplete((v, error) -> {
			System.out.println("任务2执行完毕");
		});
		
		future1.runAfterEither(future2, () -> {
			System.out.println("任务1、2任意一个执行完毕，我就开始执行");
		});
	}


	public int getMaximumGenerated(int n) {
		if (n < 2) {
			return n;
		}
		int[] arr = new int[n + 1];
		arr[0] = 0;
		arr[1] = 1;
		for (int i = 1; i < n; ++i) {
			int d = i << 1;
			if (2 <= d && d <= n) {
				arr[d] = arr[i];
			}
			if (2 <= d + 1 && d + 1 <= n) {
				arr[d + 1] = arr[i] + arr[i + 1];
			}
		}
		return Arrays.stream(arr).max().orElse(0);
	}
	@Test
	public void test1() {
		System.out.println(getMaximumGenerated(2));
	}
	
}
