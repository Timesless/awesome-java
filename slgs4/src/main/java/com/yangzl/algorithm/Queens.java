package com.yangzl.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangzl
 * @date 2019/10/26 17:25
 *
 * 解八皇后。Tips：JVM在编译时会解八皇后
 * 使用一维数组代替本该需要的二维数组。
 * int[] ary = {0, 4, 7, 5, 2, 1, 6, 3}
 * 0: 第一个queen放在第一列，4: 第二个queen放在第五列，...
 */
public class Queens {

	int[] ary;
	/**  92 */
	private int count;
	/** 15720 */
	private int loopTimes;
	private final int size;
	private final List<List<Integer>> rs;

	public Queens(int size) {
		this.size = size;
		ary = new int[size];
		rs = new ArrayList<>(10);
	}
	
	/**
	 * 2020/11/27 解n皇后
	 * 
	 * @param () this.size 
	 * @return List
	 */
	public List<List<Integer>> queens() {
		this.putQueen(0);
		System.out.printf("%d QUEENS 共有解法: %d， 冲突检测总次数: %d \n", size, count, loopTimes);
		return this.rs;
	}
	
	/**
	 * 放置第idx个queen，每次放置需校验，如果idx == size时，说明已放置所有的queen，即为一种解法
	 */
	private void putQueen(int idx) {
		if (idx == size) {
			count++;
			rs.add(Arrays.stream(ary).boxed().collect(Collectors.toList()));
			return;
		}
		/*
		 * 有点没明白
		 * 2019年11月10日 看明白了，这个for是依次从第1列开始放置queen，如果不冲突，那么递归的放置下一个皇后，
		 * 	退出条件是放第9个queen的时候，这时表名前8个皇后已经放好了。然后这里return，返回到放第8个queen的
		 * 	puttQueen(9)这个位置，继续执行 ++x，放置第x + 1列，判断是否冲突，如果不冲突，那么又得到一个解
		 * 	继续返回，继续防止，如果x遍历完毕，那么整个for循环结束，应该返回到上一层调用的地方
		 */
		for (int x = 0; x < size; ++x) {
			// 首先将第idx个queen放在第一列，即x = 0，然后校验，然后依次放置以后的列
			ary[idx] = x;
			// 不冲突，则应该继续放置queen
			if (isAttack(idx)) {
				putQueen(idx + 1);
			}
		}
	}

	/**
	 * 校验当前queen和之前摆放的所有queen是否冲突
	 * 行是不可能冲突的，因为我们每次都会在下一行放置queen
	 * 不冲突返回true
	 */
	private boolean isAttack(int idx) {
		loopTimes++;
		for (int x = 0; x < idx; ++x) {
			/*
			 * idx表示第几个queen
			 * ary[idx]表示第idx个queen放置的列数
			 * x表示idx之前的所有queen
			 * ary[x] 表示第x个queen所在的列数
			 * 当ary[idx] == x，表示列相同
			 * 当idx - x == ary[idx] - ary[x]，表示在同一斜线
			 * 这里注意一下， x表示的含义是和idx一样的。
			 */
			if (ary[idx] == ary[x] || Math.abs(idx - x) == Math.abs(ary[idx] - ary[x]))
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		Queens queen = new Queens(6);
		List<List<Integer>> rs = queen.queens();
		for (List<Integer> tmp : rs) {
			System.out.println(tmp);
		}
	}
}
