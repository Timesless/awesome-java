package com.yangzl.algorithm;

import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author yangzl
 * @date 2019/10/26 13:13
 * @desc .. 计算后缀表达式的值 逆波兰式计算器，支持括号
 * 
 * 已提供为工具类
 * TODO: BigDecimal
 * 
 * <p>
 * 中缀 1+((2+3)*4)-5 ——> 后缀 1 2 3 + 4 * + 5 -
 * 操作数栈：1
 * 1 2
 * 1 2 3
 * 1 2 3 +
 * 1 2 3 + 4
 * 1 2 3 + 4 *
 * 1 2 3 + 4 * +
 * 1 2 3 + 4 * + 5
 * 1 2 3 + 4 * + 5 -
 * 运算符栈：+
 * +(
 * +((
 * +((+
 * +(		遇见)则将该)内所有操作符出栈，压入操作数栈，且丢弃()
 * +(*
 * +		遇见)则将该)内所有操作符出栈，压入操作数栈，且丢弃()
 * -	因为符号栈中的运算符，表示被挂起，当扫描的运算符优先级<=运算符栈中的优先级，那么应该先操作栈中的运算符
 * 需要把运算符栈所有操作符出栈压入到操作数栈中
 * 
 * 
 * 
 * *****************************************************************************
 * 
 * 		中缀表达式转后缀表达式
 *  * 
 *  * (3+4)*5-6中缀表达式计算值
 *  *	
 *  * 	前缀表达式(波兰式)：-*+3456(运算符和操作数分开)，从右往左扫描，数直接入操作数栈，运算符，则弹出数栈中两个数，
 *  * 	栈顶元素 操作符 次顶元素，然后将结果入数栈，依次执行，直到表达式最左
 *  * 	
 *  * 	后缀表达式(逆波兰式)：34+5*6-，从左往右扫描(符合人类思维，多采用这种方式)。数入操作数栈，扫描到运算符，则弹出数栈中
 *  * 	两个数， 次顶元素 操作符 栈顶元素，然后将结果入数栈， 执行直到表达式最右
 *  * 	
 *  * 	声明：由于操作数栈，只有压栈操作，且还需反向才能得到结果，所以使用数组或list保存简化操作
 *  * 			以下操作数栈皆为list实现。
 */
public class StringCalculator {

	private String expression;
	private static final Pattern PATTERN = Pattern.compile("\\d+");

	public StringCalculator() {}
	/*public StringCalculator(String infix) {
		this.expression = infix;
	}*/

	/**
	 * 2020/11/27 计算this.suffix表达式的值
	 *
	 * @param () this.suffix
	 * @return int
	 */
	public int calculate() {
		return calculate(this.expression);
	}

	/**
	 * 2020/11/27 计算后缀表达式的值
	 *
	 * @param expression 后缀表达式
	 * @return int
	 */
	public int calculate(String expression) {
		List<String> suffixOp = this.infix2Suffix(expression);
		return calculateWithSuffix(suffixOp);
	}

	/**
	 * 中缀转后缀
	 * (3+4)*5-6
	 * 运算符栈
	 * 操作数栈
	 * 4种情况：(, ), 运算符，操作数
	 *
	 * @param infix 中缀表达式
	 * @return operand stack
	 */
	public List<String> infix2Suffix(String infix) {
		int x = 0, len = infix.length(), count;
		Deque<Character> operator = new ArrayDeque<>(len);
		List<String> operand = new ArrayList<>(len);
		// count代表遍历infix步长，遍历的是中缀表达式
		while (x < len) {
			// ....注意每次循环都需要将count置为1，因为在获取数字时如果改变了count，那么count永远是其他值
			count = 1;
			char tmp = infix.charAt(x);
			if (tmp == 32) {
				++ x;
				continue;
			}
			// 1 括号直接入运算符栈
			if (tmp == '(') {
				operator.push('(');
			} else if (tmp == ')') {
				// 2
				// 右括号，会出栈运算符栈，直到遇见左括号，并且丢弃该对括号
				while (operator.peek() != '(') {
					operand.add(String.valueOf(operator.pop()));
				}
				operator.pop();
			} else if (isOperator(tmp)) {
				// 3 栈顶如果是运算符那么比较优先级，有可能是括号
				Character top = operator.peek();
				if (!operator.isEmpty() && isOperator(top)) {
					comparePriority(operator, operand, tmp, top);
				} else {
					//将当前运算符压入
					operator.push(tmp);
				}
			} else {    // 4
				count = getNumber(x, infix);
				int number = Integer.parseInt(infix.substring(x, x + count));
				operand.add(String.valueOf(number));
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


	//. ================================================================================
	// divide
	// ================================================================================


	/**
	 * 2020/11/27
	 *
	 * 	 比较运算符栈和当前读到的运算符的优先级
	 * 	 栈中运算符优先级 >= 当前运算符优先级时，将运算符栈顶元素出栈，加入到操作数栈中，将当前运算符压入运算符栈
	 * 	 栈中运算符优先级 < 当前运算符优先级时，将当前运算符压入运算符栈
	 *
	 * 	 比较该运算符与栈顶运算符优先级，栈中运算符表示被挂起的意思
	 *
	 * @param operator 符号栈
	 * @param  operand 操作数栈
	 * @param  tmp 当前运算符
	 * @param  top 栈顶元素
	 */
	private void comparePriority(Deque<Character> operator, List<String> operand, char tmp, char top) {

		int stackTopPriority = getPriority(top);
		int curInfixPriority = getPriority(tmp);
		// 栈中运算符优先级 >= 当前运算符优先级时
		if (stackTopPriority >= curInfixPriority) {
			// operand.add(String.valueOf(top))
			// 所有 挂起的操作符「 >= curInfixCharPriority」都应该加到 operand 中
			int sz = operator.size();
			for (int i = 0; i < sz; ++i) {
				char front = operator.peek();
				if (getPriority(front) < curInfixPriority) {
					break;
				}
				operand.add(String.valueOf(operator.pop()));
			}
		}
		operator.push(tmp);
	}

	/**
	 * 2020/11/27 后缀表达式实现计算，只需一个操作数栈
	 * 
	 * @param suffix 后缀表达式
	 * @return int
	 */
	private int calculateWithSuffix(List<String> suffix) {
		int x = 0, size = suffix.size();
		String tmp;
		// 操作数栈
		Deque<Integer> operand = new ArrayDeque<>(size);
		for (; x < size; ++x) {
			tmp = suffix.get(x);
			if (PATTERN.matcher(tmp).matches()) {
				operand.push(Integer.parseInt(tmp));
			} else {
				/*
				 * 如果不是数字，那么就是操作符
				 * 将栈弹出两个数字，进行计算，在后缀表达式中，如果遇见运算符，那么操作数栈中肯定是有两个数的
				 */
				int top = operand.pop();
				int second = operand.pop();
				operand.push(this.stackOp(top, second, tmp));
			}
		}
		
		// 最后在操作数栈中的数即为结果
		return operand.pop();
	}

	/**
	 * 2020/11/27 进行操作数栈和符号栈顶的运算：次顶元素 运算符 栈顶元素
	 * 						
	 * @param top 操作数栈顶元素
	 * @param  second 操作数次顶元素
	 * @param  tmp 符号栈元素
	 * @return int 计算结果
	 */
	private int stackOp(int top, int second, String tmp) {
		switch (tmp) {
			case "+":
				return second + top;
			case "-":
				return second - top;
			case "*":
				return second * top;
			case "/":
				return second / top;
			default:
				break;
		}
		
		return 0;
	}

	/**
	 * 判断是否是运算符
	 */
	private boolean isOperator(char op) {
		return op == '+' || op == '-' || op == '*' || op == '/';
	}

	/**
	 * 定义运算符优先级
	 * +- = 1, * / = 2
	 */
	private int getPriority(char op) {
		int AM = 1;
		int MD = 2;
		switch (op) {
			case '+':
			case '-':
				return AM;
			case '*':
			case '/':
				return MD;
			default:
				break;
		}
		return 0;
	}

	/**
	 * 2020/11/27 获取表达式中的数值的位数
	 *
	 * 
	 * @param idx startIdx
	 * @param  infix 中缀表达式
	 * @return int 数值的位数
	 */
	private int getNumber(int idx, String infix) {
		int len = infix.length(), count = 0;
		char tmp;
		while (idx < len) {
			tmp = infix.charAt(idx);
			// 判断是否是数字
			if (tmp < 48 || tmp > 57) {
				break;
			}
			++count;
			idx++;
		}
		return count;
	}

	

	public static void main(String[] args) {
		String expression = "(3+4)*5-6";
		//StringCalculator sufix = new StringCalculator(expression);
		// 中缀转后缀，计算后缀的值
		//System.out.printf("%s = %d", expression, sufix.calculate());
	}

	@Test
	public void testString2Suffix() {
		StringCalculator calculator = new StringCalculator();
		List<String> ops = calculator.infix2Suffix("1*2-3/4+5*6-7*8+9/10");
		System.out.println(ops);
		System.out.println(calculateWithSuffix(ops));
	}
}
