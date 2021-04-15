package com.yangzl.algorithm;

import java.util.Arrays;

/**
 * @Author: yangzl
 * @Date: 2019/11/7 19:25
 * @Desc: ..	KMP算法在源字符串 "BBC ABCDAB ABCDABCDABDE"中匹配
 * 			模式串："ABCDABD"
 * 	模式串部分匹配值表（相同前缀后缀长度最大值）：	0 0 0 0 1 2 0
 * 	部分匹配值表会令源字符串i不回溯，模式串j向右移动j - next[j]，此值 >= 1
 * 		即：i不变， j = next[j]
 * 	如果next[j] = 0 | -1，那么条道模式串的开头字符
 * 	若next[j] = k，且k > 0，那么j应该跳到k个字符之前
 * 	
 * 	可以得到以下结论： 匹配时， i++, j++
 * 		1 根据最大长度表：失配时： 模式串向右移动，已匹配字符数 - 失配字符上一字符所对应的最大长度值
 * 		最大长度值对应next[]为： -1 0 0 0 0 0 1 2 （相同前缀后缀长度最大值表整体右移，初始值为-1）	
 * 		2 根据next[]：失配时，模式串向右移动 = 失配字符的位置（已匹配字符数） - 失配字符对应的next[]值	
 * 	对比1 2 发现完全一样
 * 	所以2种都可以使用。	
 * 	？？？？？？
 */
public class KMP {

	public static int kmpMatch(String dest, String subString) {
		int len1 = dest.length(), len2 = subString.length();
		if(len1 < len2) return -1;
		int[] maxCommonVal = KMP.getMaxTab(subString);
		int x = 0, y = 0;
		for (; x < len1; ++x) {
			/*
			 * KMP算法核心
			 * 如果是next[]，那么y = next[y]
			 * 为什么先判断相等的情况是错误的
			 */
			while (y > 0 && dest.charAt(x) != subString.charAt(y)) {
				y = maxCommonVal[y - 1];
				// --y;
			}
			if(dest.charAt(x) == subString.charAt(y)) { ++y; }
			// 为什么要写在循环里面
			if(y == len2) { return x - y + 1; }
		}
		// 匹配到子串
		return -1;
	}
	
	/**
	 * 计算相同前缀后缀最大长度值表
	 * 	next[]是将最大长度值表整体向右移动1位，并将next[]初始值赋值为-1
	 * --y 和 result[y - 1] 是相同的，因为y从0开始，相同则递增
	 */
	public static int[] getMaxTab(String subString) {
		int x = 1, y = 0, len = subString.length();
		int[] maxCommonVal = new int[len];
		for (; x < len; ++x) {
			if(subString.charAt(x) == subString.charAt(y)) {
				++y;
			} else {
				while (y > 0 && subString.charAt(x) != subString.charAt(y)) {
					y = maxCommonVal[y - 1];
					// --y; --y其实也可以，就成了每次y只递减一步，但i也是不回溯的
				}
			}
			maxCommonVal[x] = y;
		}
		return maxCommonVal;
	}

	public static void main(String[] args) {
		String dest = "BBC ABCDAB ABCDABCDABDE", subString = "ABCDABD";
		System.out.println(Arrays.toString(getMaxTab(subString)));
		System.out.println(kmpMatch(dest, subString));
	}
}
