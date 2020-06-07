package com.thinking5;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: yangzl
 * @Date: 2020/4/28 14:31
 * @Desc: ..
 * 
 */
public class String18 {

	public static void main(String[] args) {
		Pattern pat = Pattern.compile("(abc)+?");
		Matcher machter = pat.matcher("abcabc");
		while(machter.find())
			System.out.println(machter.group());
		
	}

}
