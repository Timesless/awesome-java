package com.leetcode.pointer;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: yangzl
 * @Date: 2020/1/19 14:44
 * @Desc: .. 
 */
public class SquareSum {

	// 判断一个数是否是两个数的平方和
	public boolean judgeSquareSum(int c) {
		if (c == 1) return true;
		int p1 = 0, p2 = (int) Math.sqrt(c), product;
		while (p1 <= p2) {
			product = p1 * p1 + p2 * p2;
			if (product == c) { return true; }
			if (product > c)
				--p2;
			else
				++p1;
		}
		return false;
	}
	
	// 反转原因字母
	public static String reverseVowels(String s) {
		if (s.length() == 1) return s;
		Set<Character> set = Stream.of('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U')
				.collect(Collectors.toCollection(HashSet::new));
		char[] c = s.toCharArray();
		int p1 = 0, p2 = c.length - 1;
		while(p1 < p2) {
			boolean f1 = set.contains(c[p1]), f2 = set.contains(c[p2]);
			if (f1 && f2) {
				char tmp = c[p1];
				c[p1] = c[p2];
				c[p2] = tmp;
				--p2;
				++p1;
			} else if (f1) {
				--p2;
			} else {
				++p1;
			}
		}
		return String.valueOf(c);
	}
	

	public static void main(String[] args) {
		// System.out.println(new SquareSum().judgeSquareSum(1000_000));
		System.out.println(reverseVowels("hello"));
	}
}
