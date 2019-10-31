package com.yinhai.datastrcture.recursion;

/**
 * @Author: yangzl
 * @Date: 2019/10/26 21:14
 * @Desc: ..	递归 + 回溯解迷宫
 * 使用一个矩阵来表示迷宫，0表示还未走过的路径，1表示路障，2表示通路，3表示走过的路径但无解
 **/
public class Maze {

	private int rows, cols;
	private int[][] matrix;
	/*
	 * 还未走过
	 * 路障
	 * 通路
	 * 该路径走不通
	 */
	char yet = 0;
	char roadBlock = 1;
	char access = 2;
	char dead = 3;
	
	public Maze(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		matrix = new int[rows][cols];
	}
	
	/*
	 * 初始化迷宫，四周为墙壁并生成固定的2个路障
	 */
	private void initMaze() {
		for(int x = 0; x < cols; ++x) {
			matrix[0][x] = roadBlock;
			matrix[rows - 1][x] = roadBlock;
			matrix[x][0] = roadBlock;
			matrix[x][rows - 1] = roadBlock;
		}
		matrix[3][1] = roadBlock;
		matrix[4][2] = roadBlock;
		print();
	}
	
	/*
	 * 解迷宫
	 * 坐标，从哪开始
	 * 需要定义规则： 下，右，上，左
	 * 走到 matrix[rows - 2][cols - 2]即为成功解
	 */
	private boolean existPath(int row, int col) {
		if(matrix[rows - 2][cols - 2] == access) {
			System.out.println("解迷宫成功...");
			print();
			return true;
		}
		if (row < rows - 1 && col < cols - 1) {
			if(matrix[row][col] == yet) {	// 如果还未走过，则可以进入，所以上面if的边界判断可删除
				// 将当前位置标记为通路
				matrix[row][col] = access;
				if(existPath(row + 1, col) || existPath(row, col + 1)
						|| existPath(row - 1, col) || existPath(row, col - 1)) {
					return true;
				}
				// 如果没有access，那么标记为dead
				matrix[row][col] = dead;
			}
		}
		return false;
	}
	
	/*
	 * 打印当前迷宫
	 */
	private void print() {
		System.out.println("迷宫情况如下...");
		for (int x = 0; x < rows; ++x) {
			for(int y = 0; y < cols; ++y) {
				System.out.printf("%d ", matrix[x][y]);
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		Maze maze = new Maze(10, 10);
		maze.initMaze();
		maze.existPath(1, 1);
	}
	
}
