package com.imooc.algorithm;

import org.junit.Test;

/**
 * @Author: yangzl
 * @Date: 2020/2/16 03:14
 * @Desc: .. 双指针
 */
public class TwoPointer {
	
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
}
