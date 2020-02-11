package com.yangzl;

import java.util.Date;

/**
 * @Author: yangzl
 * @Date: 2020/1/10 15:50
 * @Desc: .. 汇编指令
 **/
public class TestDate {

	private int count = 0;

	public static void main(String[] args) {
		TestDate testDate = new TestDate();
		testDate.test1();
	}

	public void test1() {
		Date date = new Date();
		String name1 = "wangerbei";
		test2(date, name1);
		System.out.println(date + name1);
	}

	public void test2(Date dateP, String name2) {
		dateP = null;
		name2 = "zhangsan";
	}

	public void test3() {
		count++;
	}

	public void test4() {
		int a = 0;
		{
			int b = 0;
			b = a + 1;
		}
		int c = a + 1;
	}
}