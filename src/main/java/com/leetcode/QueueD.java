package com.leetcode;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: yangzl
 * @Date: 2020/2/13 11:11
 * @Desc: .. 队列相关问题
 */
public class QueueD {

	/**
	 * @Date: 2020/2/12
	 * @Desc: 347.前k个高频元素（优先队列，标准库中为最小堆实现）
	 */
	public static List<Integer> topKFrequent(int[] nums, int k) {
		Map<Integer, Integer> map = new TreeMap<>();
		for (int tmp : nums) {
			if (map.containsKey(tmp))
				// map.compute(tmp, (ok, ov) -> ov + 1);
				map.put(tmp, map.get(tmp) + 1);
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
	@Test
	public void test1() {
		int[] nums = { 1 };
		int k = 1;
		System.out.println(topKFrequent(nums, k));
	}

	/**
	 * @Date: 2020/3/5 橘子腐烂
	 * @Desc: 队列 BFS
	 */
	public int orangesRotting(int[][] grid) {
		Deque<Integer> queue = new ArrayDeque<>();
		int m = grid.length, n = grid[0].length;
		// 记录新鲜橘子总数，腐烂分钟数
		int count = 0, round = 0;
		for (int i = 0; i < m; ++i)
			for (int j = 0; j < n; ++j) {
				if (grid[i][j] == 1)
					count++;
				else if (grid[i][j] == 2)
					queue.addLast(i * n + j);
			}

		while(count > 0 && !queue.isEmpty()) {
			++round;
			int sz = queue.size();
			for (int i = 0; i < sz; ++i) {
				int coordinate = queue.pollFirst();
				int row = coordinate / n;
				int col = coordinate % n;
				if (row >= 0 && row < m && col >= 0 && col < n) {
					int top = row - 1;
					if (top >= 0 && grid[top][col] == 1) {
						--count;
						grid[top][col] = 2;
						queue.addLast(top * n + col);
					}
					int left = col - 1;
					if (left >= 0 && grid[row][left] == 1) {
						--count;
						grid[row][left] = 2;
						queue.addLast(row * n + left);
					}
					int bottom = row + 1;
					if (bottom < m && grid[bottom][col] == 1) {
						--count;
						grid[bottom][col] = 2;
						queue.addLast(bottom * n + col);
					}
					int right = col + 1;
					if (right < n && grid[row][right] == 1) {
						--count;
						grid[row][right] = 2;
						queue.addLast(row * n + right);
					}
				}
			}
		}
		return count > 0 ? -1 : round;
	}
	@Test
	public void test2() {
		int[][] grid = new int[1][];
		grid[0] = new int[]{2, 1, 0, 2};
		System.out.println(orangesRotting(grid));
	}
	
	
}
