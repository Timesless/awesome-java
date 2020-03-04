package com.yangzl.datastrcture;

import org.junit.Test;

/**
 * @Author: yangzl
 * @Date: 2019/10/21 19:56
 * @Desc:
 * 稀疏数组：列数：固定为3，行数：数据有效数据个数 + 1
 * 第一行固定：原二维数组rows, cols, counts<元素个数>
 * 第二行开始依次存储每个元素的row, col, val
 * 可以存储到文件，再从文件读回内存
 **/
public class SparseArray {

	/*
	 * 对外提供转换为稀疏数组的函数
	 */
	public int[][] sparseArray() {
		int[][] ary = this.initAry();
		return toSparseArray(ary);
	}
	
	/*
	 * 对外提供稀疏数组转换为原数组
	 */
	public int[][] resetArray(int[][] sparse) {
		int rows = sparse[0][0], cols = sparse[0][1];
		int[][] result = new int[rows][cols];
		// 遍历稀疏数组获取值，还原数组，索引从1开始
		int curRow, curCol, curVal;
		for(int x = 1; x < sparse.length; ++x) {
			curRow = sparse[x][0];
			curCol = sparse[x][1];
			curVal = sparse[x][2];
			result[curRow][curCol] = curVal;
		}
		this.printAry(result);
		return result;
	}

	/*
	 * 随机值初始化数组
	 */
	private int[][] initAry() {
		int[][] ary = new int[11][11];
		int i = 0, x, y;
		for(; i < 5; ++i) {
			x = (int) Math.ceil(Math.random() * 10);
			y = (int) Math.ceil(Math.random() * 10);
			ary[x][y] = (int) Math.ceil(Math.random() * 32);
		}
		this.printAry(ary);
		return ary;
	}

	/*
	 * 将二维数组转换为稀疏数组
	 */
	private int[][] toSparseArray(int[][] old) {
		int x = 0, rows = 11, cols = 11;
		int count = this.getAryCount(old);
		// 创建稀疏数组，行：有效数据个数 + 1
		count++;
		int[][] sparse = new int[count][3];
		sparse[0][0] = rows;
		sparse[0][1] = cols;
		sparse[0][2] = count - 1;
		// 稀疏数组行索引
		int m = 1;
		for(; x < rows; ++x)
			for (int y = 0; y < cols; ++y) {
				if(old[x][y] != 0) {
					sparse[m][0] = x;
					sparse[m][1] = y;
					sparse[m][2] = old[x][y];
					m++;
				}
			}
		this.printAry(sparse);
		return sparse;
	}

	/*
	 * 统计原数组有效数据个数
	 */
	private int getAryCount(int[][] old) {
		int x = 0, rows = old.length, cols = old.length, count = 0;
		for(; x < rows; ++x) {
			for (int y = 0; y < cols; ++y)
				if (old[x][y] != 0) { ++count; }
		}
		return count;
	}
	
	/*
	 * 打印当前数组
	 */
	private void printAry(int[][] ary) {
		System.out.println("数组为：");
		for(int[] tmp : ary) {
			for (int item : tmp)
				System.out.printf("%2d\t", item);
			System.out.println();
		}
	}

	@Test
	public void test1() {
		int[][] rs = sparseArray();
		resetArray(rs);
	}
}
