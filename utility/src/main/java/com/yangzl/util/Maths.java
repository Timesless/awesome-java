package com.yangzl.util;

/**
 * @author yangzl
 * @date 2020/11/17 10:06
 * @desc 自实现的一些数学类的工具
 */

public class Maths {
	
	/**
	 * 2020/11/17 辗转取模法 计算最大公约数
	 * 
	 * @param p 参数一
	 * @param  q 参数二
	 * @return long 两数的最大公约数
	 */
	public static long gcd(long p, long q) {
		if (q == 0) return p;
		long r = p % q;
		return gcd(q, r);
	}

}
