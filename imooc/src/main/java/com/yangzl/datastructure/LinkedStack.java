package com.yangzl.datastructure;

/**
 * @Author: yangzl
 * @Date: 2019/10/26 12:55
 * @Desc: 带头节点单链表实现栈，链表是动态数据结构不用判断isFull
 */
public class LinkedStack<E> {
	private int size;
	private final Node<E> head;
	public LinkedStack() { this.head = new Node(-1); }

	/*
	 * 静态内部类，完全可以独立存在，形式上的“内部”，神似外部类
	 */
	private static class Node<E> {
		E val;
		Node<E> next;
		public Node(E val) { this.val = val; }
		public Node(E val, Node<E> next) {
			this.val = val;
			this.next = next;
		}
	}
	
	// 是否为空
	public boolean isEmpty() { return this.size == 0; }
	// 存储元素个数
	public int size() { return this.size; }
	
	// 应采用头插法，这样不需要反向遍历
	public void push(E val) {
		head.next = new Node<>(val, head.next);
		++size;
	}
	
	// 弹出栈顶节点
	public E pop() {
		if (isEmpty()) { throw new IndexOutOfBoundsException("栈空"); }
		E res = head.next.val;
		--size;
		head.next = head.next.next;
		return res;
	}
	
	// 查看栈顶节点的值
	public E peek() {
		if (isEmpty()) { throw new IndexOutOfBoundsException("栈空"); }
		return head.next.val;
	}
	
	@Override
	public String toString() {
		if (isEmpty()) { return "空栈"; }
		StringBuilder sb = new StringBuilder("[");
		Node<E> tmp = head.next;
		while (null != tmp.next) {
			sb.append(tmp.val).append(" -> ");
			tmp = tmp.next;
		}
		sb.append(tmp.val).append(']');
		return sb.toString();
	}

	
	public static void main(String[] args) {
		LinkedStack<Integer> stack = new LinkedStack<>();
		stack.push(1);
		stack.push(2);
		stack.push(3);
		System.out.println(stack.pop());
		System.out.println(stack.peek());
		System.out.println(stack);
		System.out.println(stack.size());
	}
	
}
