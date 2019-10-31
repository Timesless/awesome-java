package com.yinhai.datastrcture.polish;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * @Author: yangzl
 * @Date: 2019/10/26 13:13
 * @Desc: ..逆波兰式计算器，支持(
 * 
 * 	中缀 1+((2+3)*4)-5 ——> 后缀 1 2 3 + 4 * + 5 -
 * 操作数栈：1
 *  		1 2
 *  		1 2 3
 *  		1 2 3 +
 *  		1 2 3 + 4
 *  		1 2 3 + 4 *	
 *  		1 2 3 + 4 * +
 *  		1 2 3 + 4 * + 5
 *  		1 2 3 + 4 * + 5 -	
 * 运算符栈：+
 *  		+(
 *  		+((
 *  		+((+
 *  		+(		遇见)则将该)内所有操作符出栈，压入操作数栈，且丢弃()
 *  		+(*
 *  		+		遇见)则将该)内所有操作符出栈，压入操作数栈，且丢弃()
 *  		-	因为符号栈中的运算符，表示被挂起(优先级比扫描到的低)，所以当扫描到的运算符优先级<=运算符栈中的优先级，那么应该先操作栈中的运算符
 *  		 	需要把运算符栈所有操作符出栈压入到操作数栈中	
 *  
 **/
public class SuffixCalculator {
	
	/*
	 * 后缀表达式实现计算，只需一个操作数栈
	 * 以下实现未考虑用户错误输入。
	 */
	int calculateWithSuffix(List<String> suffix) {
		int x = 0, size = suffix.size();
		String tmp;
		Deque<Integer> operand = new ArrayDeque<>(size);
		for (; x < size; ++x) {
			tmp = suffix.get(x);
			if(tmp.matches("\\d+")) {
				operand.push(Integer.valueOf(tmp));
			} else {
				/*
				 * 如果不是数字，那么就是操作符
				 * 将栈弹出连个数字，进行计算，在后缀表达式中，如果遇见运算符，那么操作数栈中肯定是有两个数的
				 */
				int top = operand.pop();
				int second = operand.pop();
				operand.push(this.calculate(top, second, tmp));
			}
		}
		// 最后在操作数栈中的数即为结果
		return operand.pop();
	}


	/*
	 *  次顶元素 运算符 栈顶元素
	 */
	private int calculate(int top, int second, String tmp) {
		int rs = 0;
		switch (tmp) {
			case "+":
				rs = second + top;
				break;
			case "-":
				rs = second - top;
				break;
			case "*":
				rs = second * top;
				break;
			case "/":
				rs = second / top;
				break;
			default:
				break;
		}
		return rs;
	}


	public static void main(String[] args) {
		InfixCalculator infix = new InfixCalculator();
		String expression = "(3+4)*5-6";
		SuffixCalculator sufix = new SuffixCalculator();
		System.out.printf("%s = %d", expression,
				sufix.calculateWithSuffix(infix.infix2Suffix(expression)));
	}
	
}
