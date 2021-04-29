package com.yangzl;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author yangzl
 * @date 2020/4/10 23:56
 *
 * 矩阵相关问题
 */
public class Matrix {

	// =======================================================================
	// 顺时针旋转矩阵90°
	// =======================================================================

	public void rotateMatrix(int[][] matrix) {
		int m;
		if ((m = matrix.length)== 0) return;
		int half = m >>> 1;
		// 先按照对角线翻转矩阵，再按照中轴线翻转矩阵
		for (int i = 0; i < m; ++i) {
			for (int j = i; j < m; ++j) {
				int tmp = matrix[i][j];
				matrix[i][j] = matrix[j][i];
				matrix[j][i] = tmp;
			}
			for (int x = 0; x < half; ++x) {
				int tmp = matrix[i][x];
				matrix[i][x] = matrix[i][m - 1 - x];
				matrix[i][m - 1 - x] = tmp;
			}
		}
	}
	@Test
	public void testRotateMatrix() {
		int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
		rotateMatrix(matrix);
		for (int[] tmp : matrix) {
			System.out.println(Arrays.toString(tmp));
		}
	}


	// =======================================================================
	// 
	// =======================================================================

}
