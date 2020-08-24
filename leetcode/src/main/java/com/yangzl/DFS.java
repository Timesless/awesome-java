package com.yangzl;

import org.junit.Test;

/**
 * @Author: yangzl
 * @Date: 2020/4/8 14:38
 * @Desc: .. 广度优先
 */
public class DFS {


	// =======================================================================
	// 13. 地上有一个m行n列的方格，从坐标 [0,0] 到坐标 [m-1,n-1] 。
	// 一个机器人从坐标 [0, 0] 的格子开始移动，它每次可以向左、右、上、下移动一格（不能移动到方格外），也不能进入行坐标和列坐标的数位之和大于k的格子
	// 例如，当k为18时，机器人能够进入方格 [35, 37] ，因为3+5+3+7=18。
	// 但它不能进入方格 [35, 38]，因为3+5+3+8=19。请问该机器人能够到达多少个格子？
	// 此题也可使用 BFS解
	
	// 递归式调用边界判断时机 & 非递归式调用的边界判断时机
	
	// =======================================================================
	int count, k;
	boolean[][] visited;
	// 4个方向
	int[][] dirs = { {-1, 0}, {0, 1}, {1, 0}, {0, -1} };
	public int movingCount(int m, int n, int k) {
		this.visited = new boolean[m][n];
		this.k = k;
		// 
		dfs(0, 0, m, n);
		return count;
	}
	private void dfs(int row, int col, int rows, int cols) {
		// 基准情形
		if (row < 0 || row >= rows || col < 0 || col >= cols
				|| visited[row][col] || !canArrive(row, col))
			return;

		++ count;
		System.out.printf("x = %d, y = %d\n", row, col);
		visited[row][col] = true;
		for (int i = 0; i < 4; ++i) {
			int nx = row + dirs[i][0];
			int ny = col + dirs[i][1];
			dfs(nx, ny, rows, cols);
		}
	}
	private boolean canArrive(int x, int y) {
		int smx = 0, smy = 0;
		while (x > 0) {
			int mod = x % 10;
			x /= 10;
			smx += mod;
		}
		while (y > 0) {
			int mod = y % 10;
			y /= 10;
			smy += mod;
		}
		return this.k >= smx + smy;
	}
	@Test
	public void testMovingCount() {
		System.out.println(movingCount(3, 3, 4));
	}

	
	// =======================================================================
	// 39
	// =======================================================================

	
	// =======================================================================
	// 39
	// =======================================================================

	
	// =======================================================================
	// 39
	// =======================================================================

}
