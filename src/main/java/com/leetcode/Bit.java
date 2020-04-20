package com.leetcode;

import org.junit.Test;

import java.util.Arrays;

/**
 * @Author: yangzl
 * @Date: 2020/3/31 14:38
 * @Desc: .. 位运算
 */
public class Bit {
	
	/**
	 * @Date: 2020/3/31
	 * @Desc:  位运算实现加法
	 */
	public int bitAdd(int a, int b) {
		while (b != 0) {
			int nonCarry = a ^ b;
			int carry = (a & b) << 1;
			a = nonCarry;
			b = carry;
		}
		return a;
	}
	@Test
	public void testBitAdd() {
		System.out.println(bitAdd(-3, 2));
		System.out.println(Integer.toBinaryString(-1));
	}
	
	@Test
	public void test1() {
		String[] arrs = "00011111".split("0");
		for (int i = 0; i < arrs.length; i++) {
			System.out.println(arrs[i].length());
		}
		System.out.println(Arrays.toString(arrs));
	}

}
