package com.yangzl.algorithm;

/**
 * @Author yangzl
 * @Date 2019/10/26 21:14
 * @Desc ..	递归 + 回溯解迷宫
 * 使用一个矩阵来表示迷宫，0表示还未走过的路径，1表示路障，2表示通路，3表示走过的路径但无解
 */
public class Maze {
	private final int rows, cols;
	private final int[][] matrix;
	/*
	 * 0还未走过 1路障 2通路 3该路径走不通
	 */
	static final char YET = 0, ROAD_BLOCK = 1, ACCESS = 2, DEATH = 3;
	
	public Maze(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		matrix = new int[rows][cols];
	}
	
	/**
	 * 初始化迷宫，四周为墙壁并生成固定的2个路障
	 */
	private void initMaze() {
		for(int x = 0; x < cols; ++x) {
			matrix[0][x] = ROAD_BLOCK;
			matrix[rows - 1][x] = ROAD_BLOCK;
			matrix[x][0] = ROAD_BLOCK;
			matrix[x][rows - 1] = ROAD_BLOCK;
		}
		matrix[3][1] = ROAD_BLOCK;
		matrix[4][2] = ROAD_BLOCK;
		print();
	}
	
	/*
	 * 解迷宫
	 * 坐标，从哪开始
	 * 需要定义规则： 下，右，上，左
	 * 走到 matrix[rows - 2][cols - 2]即为成功解
	 */
	private boolean existPath(int row, int col) {
		if(matrix[rows - 2][cols - 2] == ACCESS) {
			System.out.println("解迷宫成功...");
			print();
			return true;
		}
		if (row < rows - 1 && col < cols - 1) {
			if(matrix[row][col] == YET) {	// 如果还未走过，则可以进入，所以上面if的边界判断可删除
				// 将当前位置标记为通路
				matrix[row][col] = ACCESS;
				if (existPath(row + 1, col) || existPath(row, col + 1)
						|| existPath(row - 1, col) || existPath(row, col - 1)) {
					return true;
				}
				// 如果没有access，那么标记为dead
				matrix[row][col] = DEATH;
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
			for(int y = 0; y < cols; ++y) { System.out.printf("%d ", matrix[x][y]); }
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		Maze maze = new Maze(10, 10);
		maze.initMaze();
		maze.existPath(1, 1);
	}
}
