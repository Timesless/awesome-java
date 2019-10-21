package com.yinhai.datastrcture.array;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * @Author: yangzl
 * @Date: 2019/10/21 19:56
 * @Desc:
 * 稀疏数组：列数固定为3，第一行固定：原二维数组rows, cols, counts<元素个数>
 * 第二行开始依次存储每个元素的row, col, val
 **/
public class SparseArray {

	int[][] sparseArray() {
		// 随机值初始化数组
		int[][] ary = new int[11][11];
		int i = 0, x, y;
		for(; i < 10; ++i) {
			x = (int) Math.ceil(Math.random() * 10);
			y = (int) Math.ceil(Math.random() * 10);
			ary[x][y] = x + y;
		}
		 return toSparseArray(ary);
	}
	
	int[][] toSparseArray(int[][] old) {
		int x = 0, count = 1, rows = 11, cols = 11;
		for(; x < rows; ++x)
			for (int y = 0; y < cols; ++y)
				if (old[x][y] != 0)
					++count;
		int[][] sparse = new int[count][3];
		sparse[0][0] = rows;
		sparse[0][1] = cols;
		sparse[0][2] = count;
		x = 0;
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
		return sparse;
	}
	
	@Test
	public void test1() {
		int[][] rs = sparseArray();
		for(int[] tmp : rs) {
			for(int item: tmp) 
				System.out.printf("%d\t", item);
			System.out.println();
		}
	}
	
}
