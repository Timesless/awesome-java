package com.yangzl.effective;

import java.util.function.DoubleBinaryOperator;

/**
 * @Author yangzl
 * @date 2020/3/3 10:51
 * @desc .. 42 lambda
 */
public enum  Operator {
	
	/** 加减乘除 */
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
	 * @param x op1
	 * @param y op2   
	 * @return double
	 */
	public double apply(double x, double y) { return binaryOp.applyAsDouble(x, y); }


	public static void main(String[] args) {
		System.out.println(Operator.PLUS.apply(2, 5));
	}
}
