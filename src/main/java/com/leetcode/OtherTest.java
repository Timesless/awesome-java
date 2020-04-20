package com.leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: yangzl
 * @Date: 2020/4/3 14:46
 * @Desc: .. 比较杂乱的一些测试
 */
public class OtherTest {
	
	private void listInitial() {
		List<String> list = new ArrayList() { {add("1");} };
	}

	String[] keyboards = {"QWERTYUIOPqwertyuiop", "ASDFGHJKLasdfghjkl", "ZXCVBNMzxcvbnm"};
	public String[] findWords(String[] words) {
		List<String> rs = new ArrayList<>(words.length);
		for (String keyborad : keyboards) {
			for (String word : words) {
				boolean flag = true;
				for (char c : word.toCharArray())
					if (keyborad.indexOf(c) < 0) {
						flag = false;
						break;
					}
				if (flag) rs.add(word);
			}
		}
		return rs.stream().toArray(String[]::new);
	}
	@Test
	public void testFindWords() {
		String[] arr = {"Hello", "Alaska", "Dad", "Peace"};
		System.out.println(Arrays.toString(findWords(arr)));
	}
	
	@Test
	public void test1() {
		System.out.println("he".substring(2));
	}

	// =======================================================================
	// 
	// =======================================================================

}
