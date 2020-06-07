package com.imooc;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * @Author: yangzl
 * @Date: 2020/3/29 11:57
 * @Desc: .. 广度优先搜索
 */
public class BFS {


	// =======================================================================
	// 1162 多源广度优先
	// 你现在手里有一份大小为 N x N 的『地图』（网格） grid，上面的每个『区域』（单元格）都用 0 和 1 标记好了。其中 0 代表海洋，1 代表陆地
	// 请返回该海洋区域到离它最远的陆地区域的距离。如果我们的地图上只有陆地或者海洋，返回 -1
	//我们这里说的距离是『曼哈顿距离』（ Manhattan Distance）：(x0, y0) 和 (x1, y1) 这两个区域之间的距离是 |x0 - x1| + |y0 - y1| 。
	// =======================================================================
	public int maxDistance(int[][] grid) {

		// 多源广度优先
		int m = grid.length, n = grid[0].length;
		Queue<Integer> q = new ArrayDeque<>();
		// count判断是否所有都是陆地，dis记录搜索轮次，因为4方向搜索符和曼哈顿距离的定义所以不必单独计算曼哈顿距离
		int count = 0, dis = -1;
		// 将所有源点（陆地）作为第一层
		for (int i = 0; i < m; ++i)
			for (int j = 0; j < n; ++j) {
				if (grid[i][j] == 1) {
					++count;
					// 状态压缩
					q.offer(i * n + j);
				}
			}
		if (count == 0 || count == m * n) return -1;
		while (!q.isEmpty()) {
			for (int i = 0, sz = q.size(); i < sz; ++i) {
				int val = q.poll();
				int row = val / n, col = val % n;
				if (row >= 0 && row < m && col >= 0 && col < n) {
					// 上下方向
					int top = row - 1, bot = row + 1;
					if (top >= 0 && grid[top][col] == 0) {
						grid[top][col] = 1;
						q.offer(top * n + col);
					}
					if (bot < m && grid[bot][col] == 0) {
						grid[bot][col] = 1;
						q.offer(bot * n + col);
					}
					// 左右方向
					int left = col - 1, right = col + 1;
					if (left >= 0 && grid[row][left] == 0) {
						grid[row][left] = 1;
						q.offer(row * n + left);
					}
					if (right < n && grid[row][right] == 0) {
						grid[row][right] = 1;
						q.offer(row * n + right);
					}
				}
			}
			// 当把最后一个海洋修改为陆地时，添加进队列，所以会多访问一次
			++dis;
			for (int[] tmp : grid)
				System.out.println(Arrays.toString(tmp));
			System.out.println();
		}
		return dis;
	}
	@Test
	public void testMaxDistance() {
		int[][] grid = new int[3][3];
		for (int i = 0; i < 3; ++i)
			Arrays.fill(grid[i], 0);
		grid[0][0] = 1;
		System.out.printf("count = %d", maxDistance(grid));
	}

	// =======================================================================
	// 994 腐烂的橘子 多源广度优先
	// 在给定的网格中，每个单元格可以有以下三个值之一：
	// 值 0 代表空单元格；值 1 代表新鲜橘子；值 2 代表腐烂的橘子。每分钟，任何与腐烂的橘子（在 4 个正方向上）相邻的新鲜橘子都会腐烂。
	// 返回直到单元格中没有新鲜橘子为止所必须经过的最小分钟数。如果不可能，返回 -1
	// =======================================================================
	public int orangesRotting(int[][] grid) {
		int rows = grid.length, cols = grid[0].length;
		int freshCount = 0, dis = -1;
		Queue<Integer> q = new ArrayDeque<>();
		for (int i = 0; i < rows; ++i)
			for (int j = 0; j < cols; ++j) {
				if (grid[i][j] == 1)
					++ freshCount;
				else if (grid[i][j] == 2)
					// 状态压缩
					q.offer(i * cols + j);
			}
		
		while (freshCount > 0 && !q.isEmpty()) {
			++ dis;
			// 多源遍历
			for (int i = 0, sz = q.size(); i < sz; ++i) {
				int val = q.poll(), row = val / cols, col = val % cols;
				// 在矩阵范围内
				if (row >= 0 && rows > row && col >= 0 && cols > col) {
					int top = row - 1, bot = row + 1;
					// 如果是新鲜橘子
					if (top >= 0 && grid[top][col] == 1) {
						-- freshCount;
						// 置为腐烂橘子，避免下次遍历。 也可以使用visited数组
						grid[top][col] = 2;
						q.offer(top * cols + col);
					}
					if (rows > bot && grid[bot][col] == 1) {
						-- freshCount;
						grid[bot][col] = 2;
						q.offer(bot * cols + col);
					} 
					int left = col - 1, right = col + 1;
					if (left >= 0 && grid[row][left] == 1) {
						-- freshCount;
						grid[row][left] = 2;
						q.offer(row * cols + left);
					}
					if (cols > right && grid[row][right] == 1) {
						-- freshCount;
						grid[row][right] = 2;
						q.offer(row * cols + right);
					}
				}
			}
		}
		
		return freshCount > 0 ? -1 : dis;
	}




	// =======================================================================
	// 
	// =======================================================================



}
