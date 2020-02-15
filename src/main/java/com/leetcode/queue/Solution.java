package com.leetcode.queue;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: yangzl
 * @Date: 2020/2/13 11:11
 * @Desc: ..
 */
public class Solution {

	/**
	 * @Date: 2020/2/12
	 * @Desc: 347.前k个高频元素
	 **/
	public static List<Integer> topKFrequent(int[] nums, int k) {
		Map<Integer, Integer> map = new TreeMap<>();
		for (int tmp : nums) {
			if (map.containsKey(tmp))
				map.compute(tmp, (ok, ov) -> ov + 1);
			else
				map.put(tmp, 1);
		}
		// 匿名内部类或局部类可以捕获final，effctively变量，所以直接使用map
		PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(map::get));
		// 存入map，只存了k，但却是按v排序的
		map.keySet().forEach(key -> queue.offer(key));
		int remain = queue.size() - k;
		for (int i = 0; i < remain; i++)
			queue.poll();
		return queue.stream().sorted().collect(Collectors.toList());
	}




	// ====================================================================
	// divide line
	// ====================================================================
	@Test
	public void test1() {
		int[] nums = { 1 };
		int k = 1;
		System.out.println(topKFrequent(nums, k));
	}
	
}
