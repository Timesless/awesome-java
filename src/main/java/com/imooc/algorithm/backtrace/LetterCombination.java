package com.imooc.algorithm.backtrace;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yangzl
 * @Date: 2020/2/18 00:35
 * @Desc: .. 字母所有组合
 */
public class LetterCombination {

	ArrayList<String> rs = new ArrayList<>();
	final String[] letters = {" ", "", "abc", "def",
			"ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

	/**
	 * @Date: 2020/2/18
	 * @Desc: 组合9键的字母
	 **/
	public List<String> letterCombinations(String digits) {
		if (digits.length() == 0)
			return rs;
		doCombinations(digits, 0, "");
		return rs;
	}

	private void doCombinations(String digits, int idx, String str) {
		if (idx == digits.length()) {
			rs.add(str);
			return;
		}
		String s = letters[digits.charAt(idx) - '0'];
		for (int i = 0; i < s.length(); i++) 
			doCombinations(digits, idx + 1, str + s.charAt(i));
	}

	@Test
	public void test() {
		letterCombinations("23");
		System.out.println(rs);
	}

}
