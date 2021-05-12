package com.yangzl;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author yangzl
 * @date 2020/4/8 14:39
 *
 * 广度优先 BFS 借助队列
 */
public class BFS {

	/**
	 * @date 2020/3/5 橘子腐烂
	 *  多源广度优先
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
	public void testOrangeRotting() {
		int[][] grid = new int[1][];
		grid[0] = new int[]{2, 1, 0, 2};
		System.out.println(orangesRotting(grid));
	}

}
