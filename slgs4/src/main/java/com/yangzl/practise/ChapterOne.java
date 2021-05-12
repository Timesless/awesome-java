package com.yangzl.practise;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author yangzl
 * @date 2020/11/17 15:36
 *
 * 第一章
 * 
 * 	1.1 练习题
 */

public class ChapterOne {

	/**
	 * 2020/11/17 原地转置矩阵
	 *
	 * @param matrix 矩阵
	 * @return int[][] 转置后的矩阵 
	 */
	public int[][] rotateMatrix(int[][] matrix) {
		assert null != matrix : "matrix must non null";
		int row = matrix.length, col = matrix[0].length;
		int[][] rs = new int[col][row];
		for (int i = 0; i < row; ++i) {
			for (int j = 0; j < col; ++j) {
				rs[j][i] = matrix[i][j];
			}
		}
		return rs;
	}
	@Test
	public void testRotateMatrix() {
		int[][] matrix = new int[3][4];
		for (int i = 0; i < matrix.length; ++i)
			for (int j = 0; j < matrix[0].length; ++j)
				matrix[i][j] = i + j;
		for (int[] tmp : matrix) {
			System.out.printf("%s\n", Arrays.toString(tmp));
		}
		System.out.println("==== 转置 ====");
		int[][] rotate = rotateMatrix(matrix);
		for (int[] tmp : rotate) {
			System.out.printf("%s\n", Arrays.toString(tmp));
		}
	}
	
	public String eR(int n) {
		return n <= 0 ? "" : eR(n - 3) + n + eR(n - 2) + n;
	}
	@Test
	public void testER() {
		System.out.println(eR(6));
	}
}
