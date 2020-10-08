package com.yangzl.algorithm;

import java.util.*;

/**
 * @author yangzl
 * @date 2019/10/26 12:57
 * @desc 中缀表达式转后缀表达式
 * 
 * (3+4)*5-6中缀表达式计算值
 *	
 * 	前缀表达式(波兰式)：-*+3456(运算符和操作数分开)，从右往左扫描，数直接入操作数栈，运算符，则弹出数栈中两个数，
 * 	栈顶元素 操作符 次顶元素，然后将结果入数栈，依次执行，直到表达式最左
 * 	
 * 	后缀表达式(逆波兰式)：34+5*6-，从左往右扫描(符合人类思维，多采用这种方式)。数入操作数栈，扫描到运算符，则弹出数栈中
 * 	两个数， 次顶元素 操作符 栈顶元素，然后将结果入数栈， 执行直到表达式最右
 * 	
 * 	声明：由于操作数栈，只有压栈操作，且还需反向才能得到结果，所以使用数组或list保存简化操作
 * 			以下操作数栈皆为list实现。
 */
public class Infix2Suffix {
	
	/*
	 * 中缀转后缀
	 * (3+4)*5-6
	 * 运算符栈
	 * 操作数栈
	 * 4种情况：(, ), 运算符，\操作数
	 */
	List<String> infix2Suffix(String infix) {
		
		int x = 0, len = infix.length(), count;
		Deque<Character> operator = new ArrayDeque<>(len);
		List<String> operand = new ArrayList<>(len);
		// count代表遍历infix步长，遍历的是中缀表达式
		for (; x < len;) {
			// ....注意每次循环都需要将count置为1，因为在获取数字时如果改变了count，那么count永远是其他值
			count = 1;
			char tmp = infix.charAt(x);
			// 1 括号直接入运算符栈
			if(tmp == '(') {
				operator.push('(');
			} else if (tmp == ')') {  // 2
				// 右括号，会出栈运算符栈，直到遇见左括号，并且丢弃该对括号
				while (operator.peek() != '(') {
					operand.add(operator.pop().toString());
				}
				operator.pop();
			} else if(isOperator(tmp)) {	// 3 栈顶如果是运算符那么比较优先级，有可能是括号
				Character top = operator.peek();
				if(!operator.isEmpty() && isOperator(top)) {
					comparePriority(operator, operand, tmp, top);
				} else {	//将当前运算符压入
					operator.push(tmp);
				}
			} else {	// 4
				count = getNumber(x, infix);
				// 最后一位数字，这里会越界
				String number = infix.substring(x, x + count);
				operand.add(number);
			}
			x += count;
		}
		/*
		 * 最后将运算符栈中所有运算符，依次弹出加入到操作数栈中
		 */
		while (!operator.isEmpty()) {
			operand.add(String.valueOf(operator.pop()));
		}
		return operand;
	}

	/*
	 * 比较运算符栈和当前读到的运算符的优先级
	 * 栈中运算符优先级 >= 当前运算符优先级时，将运算符栈顶元素出栈，加入到操作数栈中，将当前运算符压入运算符栈
	 * 栈中运算符优先级 >= 当前运算符优先级时，将当前读到的运算符加入到操作数栈中<注意，这里是list>
	 */
	private void comparePriority(Deque<Character> operator, List<String> operand, char tmp, Character top) {
		// 比较该运算符与栈顶运算符优先级，栈中运算符表示被挂起的意思
		int stackTopPriority = getPriority(top);
		int curInfixPriority = getPriority(tmp);
		// 栈中运算符优先级 >= 当前运算符优先级时
		if(stackTopPriority >= curInfixPriority) {
			operand.add(String.valueOf(operator.pop()));
			operator.push(tmp);
		} else
			operand.add(String.valueOf(tmp));
	}

	/*
	 * 判断是否是运算符
	 */
	boolean isOperator(char op) {
		boolean flag = false;
		if(op == '+' || op== '-' || op == '*' || op == '/') { flag = true; }
		return flag;
	}
	
	/*
	 * 获取运算符优先级
	 */
	int getPriority(char op) {
		// +-为1， */为2
		int AM = 1;
		int MD = 2;
		int rs = 0;
		switch (op) {
			case '+':
			case '-':
				rs = AM;
				break;
			case '*':
			case '/':
				rs = MD;
				break;
			default:
				break;
		}
		return rs;
	}
	
	/*
	 * 获取单 | 多位数
	 * 这里可以使用正则来截取数字
	 */
	int getNumber(int idx, String infix) {
		int len = infix.length(), count = 0;
		char tmp;
		while (idx < len) {
			tmp = infix.charAt(idx);
			// 判断是否是数字
			if(tmp >= 48 && tmp <= 57)
				++count;
			else 
				break;
			idx++;
		}
		return count;
	}
	
	public static void main(String[] args) {
		Infix2Suffix caclulator = new Infix2Suffix();
		String expression = "(3+4)*5-6";
		List<String> suffix = caclulator.infix2Suffix(expression);
		System.out.println(suffix);
	}
}
