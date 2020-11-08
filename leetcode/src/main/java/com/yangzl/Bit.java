package com.yangzl;

import org.junit.jupiter.api.Test;

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


	/**
	 * @Date: 2020/4/4
	 * @Desc: 交换数字转换为二进制后的奇偶数位的数值
	 */
	public int exchangeBits(int num) {
		String bs = Integer.toBinaryString(num);
		StringBuilder builder = null;
		int bln = bs.length();
		if ((bln & 1) == 1) {
			builder = new StringBuilder(++ bln);
			bs = "0" + bs;
		} else
			builder = new StringBuilder(bln);
		for (int i = 0; i < bln - 1; i += 2)
			builder.append(bs.charAt(i + 1)).append(bs.charAt(i));
		int rs = 0, count = 0;
		for (int i = bln - 1; i >= 0; --i)
			// count ++ 这里为2条指令：iload -> count的值压入操作数栈；iinc
			rs += builder.charAt(i) - 48 << (count ++);
		return rs;
	}
	@Test
	public void testExchangeBits() {
		System.out.println(exchangeBits(6));
		// 提取所有偶数位，并移动到奇数位
		int even = (6 & 0Xaaaaaaaa) >> 1;
		// 提取所有奇数位，并移动到偶数位
		int odd = (6 & 0X55555555) << 1;
		System.out.println(even | odd);
		// 1010	=> 0010
		// 0110
		// 0101	=> 0100
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
