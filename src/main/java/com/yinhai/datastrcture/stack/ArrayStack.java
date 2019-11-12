package com.yinhai.datastrcture.stack;

/**
 * @Author: yangzl
 * @Date: 2019/10/26 12:55
 * @Desc: 数组实现栈
 **/
public class ArrayStack {

	// 栈中数据个数，栈顶指针
	private int size, top = -1;
	private int[] ele;
	
	public ArrayStack(int size) { 
		this.ele = new int[size];
	}
	
	/*
	 * 可以不声明size，使用top来判断
	 * isFull top == ele.length - 1;
	 * isEmpty top == -1
	 */
	public boolean isFull() { return size == ele.length; }
	public boolean isEmpty() { return size == 0; }
	
	// 入栈
	public void push(int val) {
		if(isFull())
			throw new IndexOutOfBoundsException("栈满");
		ele[++top] = val;
		size++;
	}
	
	// 出栈
	public int pop() {
		if(isEmpty())
			throw new IndexOutOfBoundsException("空栈");
		size--;
		return ele[top--];
	}
	
	// 查看栈顶元素
	private int peek() { return ele[top]; }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int x = top; x >= 0; --x)
			sb.append(ele[x]).append(' ');
		return sb.toString();
	}
	
	public static void main(String[] args) {
		ArrayStack stack = new ArrayStack(10);
		stack.push(1);
		stack.push(2);
		stack.push(3);
		System.out.println(stack);
		System.out.println(stack.pop());
		System.out.println(stack.pop());
		System.out.println(stack);
	}
}
