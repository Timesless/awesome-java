package com.yangzl.twopointer;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: yangzl
 * @Date: 2020/3/6 14:35
 * @Desc: .. 双指针 => 对撞指针
 */
public class CollidePointer {
	// 判断一个数是否是两个数的平方和
	public boolean judgeSquareSum(int c) {
		if (c == 1) return true;
		int p1 = 0, p2 = (int) Math.sqrt(c);
		while (p1 <= p2) {
			int product = p1 * p1 + p2 * p2;
			if (product == c) { return true; }
			if (product > c)
				--p2;
			else
				++p1;
		}
		return false;
	}

	// 反转元音字母
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
		return c.toString();
	}
	@Test
	public void testReverseVowels() {
		System.out.println(reverseVowels("hello"));
	}

	
	/**
	 * @Date: 2020/2/16 是否是回文字符串
	 * @Desc:  东拼西凑
	 **/
	public boolean isPalindrome(String s) {
		int i = 0, j = s.length() - 1;
		while (i < j) {
			char lc = s.charAt(i);
			char rc = s.charAt(j);
			if (!((lc>=65 && lc <=90) || (lc>=97 && lc<= 122) || (lc>=48 && lc<=57)))
				i++;
			else if (!((rc>=65 && rc <=90) || (rc>=97 && rc<= 122) || (rc>=48 && rc<=57)))
				j--;
			else {
				if (lc > 57 && rc > 57) {
					if (lc == rc + 32 || lc == rc -32 || lc == rc) {
						i++;
						j--;
					} else
						return false;
				} else if (lc == rc) {
					i++;
					j--;
				} else
					return false;
			}
		}
		return true;
	}
	@Test
	public void testIsPalindrome() {
		System.out.println(isPalindrome("0P"));
	}


	// =======================================================================
	// 11. 盛最多水的容器，给定数组，数组每一个元素与 x 轴共同构成的容器可以容纳最多的水。
	// =======================================================================
	public int maxArea(int[] height) {
		int len = height.length;
		if (len < 2) return 0;
		int max = 0, left = 0, right = len - 1;
		while (left < right) {
			int diff = right - left;
			max = height[left] < height[right] ? Math.max(max, diff * height[left++])
					: Math.max(max, diff * height[right--]);
		}
		return max;
	}
	
}
