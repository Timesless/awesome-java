package com.yinhai.datastrcture.recursion;

import java.util.Arrays;

/**
 * @Author: yangzl
 * @Date: 2019/10/26 17:25
 * @Desc: .. 解八皇后
 * 	使用一维数组代替本该需要的二维数组。
 * 	int[] ary = {0, 4, 7, 5, 2, 1, 6, 3}
 * 	第一个queen放在第一列，第二个queen放在第五列，...
 **/
public class Queens8 {

	int size = 8;
	int[] ary = new int[size];
	private int count;	// 92
	private int allCount;	// 15720
	
	/*
	 * 放置第idx个queen，此时应该从第一列开始放置，每次放置需校验
	 */
	void putQueen(int idx) {
		if(idx == size) {
			count++;
			System.out.println(Arrays.toString(ary));
			return;
		}
		// 
		for(int x = 0; x < size; ++x) {
			// 首先将第idx个queen放在第一列，然后校验，然后依次放置以后的列
			ary[idx] = x;
			// 如果冲突，那么应该重新放置
			if(isAttack(idx))
				putQueen(idx + 1);
			// 不冲突，则应该继续放置queen
		}
		
	}
	
	/*
	 * 校验当前queen和之前摆放的所有queen是否冲突
	 * 行是不可能冲突的，因为我们每次都会在下一行放置queen
	 */
	boolean isAttack(int idx) {
		allCount++;
		for(int x = 0; x < idx; ++x) {
			/*
			 * idx表示第几个queen
			 * ary[idx]表示第idx个queen放置的列数
			 * x表示idx之前的所有queen
			 * ary[x] 表示第x个queen所在的列数
			 * 当ary[idx] == x，表示列相同
			 * 当idx - x == ary[idx] - ary[x]，表示在同一斜线
			 * 这里注意一下， x表示的含义是和idx一样的。
			 */
			if(ary[idx] == ary[x] || Math.abs(idx - x) == Math.abs(ary[idx] - ary[x]))
				return false;
		}
		return true;
	}


	public static void main(String[] args) {
		Queens8 queen = new Queens8();
		// 从第0个queen开始放
		queen.putQueen(0);
		System.out.println(queen.count);
		System.out.println(queen.allCount);
	}
	
	
}
