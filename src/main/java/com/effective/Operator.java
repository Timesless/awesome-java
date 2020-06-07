package com.effective;

import java.util.function.DoubleBinaryOperator;

/**
 * @Author: yangzl
 * @Date: 2020/3/3 10:51
 * @Desc: .. 42 lambda
 */
public enum  Operator {
	PLUS ("+", (x, y) -> x + y),
	MINUS ("-", (x, y) -> x - y),
	TIMES ("*", (x, y) -> x * y),
	DIVIDE ("/", (x, y) -> x / y);

	private final String symbol;
	private final DoubleBinaryOperator binaryOp;
	Operator(String symbol, DoubleBinaryOperator binaryOp) {
		this.symbol = symbol;
		this.binaryOp = binaryOp;
	}
	/**
	 * 2020/6/7
	 * @param
	 * @return
	 */
	public double apply(double x, double y) { return binaryOp.applyAsDouble(x, y); }


	public static void main(String[] args) {
		System.out.println(Operator.PLUS.apply(2, 5));
	}
}
