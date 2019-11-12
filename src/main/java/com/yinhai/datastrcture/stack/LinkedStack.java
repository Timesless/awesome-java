package com.yinhai.datastrcture.stack;

/**
 * @Author: yangzl
 * @Date: 2019/10/26 12:55
 * @Desc: 单链表实现栈
 * 		与数组实现栈不同，这里采用0作为初始值，由于链表实现，所以未实现判断满操作
 **/
public class LinkedStack {

	private int size;
	private Node head;
	public LinkedStack() {
		this.head = new Node(-1);
	}
	
	public boolean isEmpty() { return this.size == 0; }
	
	// 应采用头插法，这样不需要反向遍历
	public void push(int val) {
		Node newNode = new Node(val);
		newNode.next = head.next;
		head.next = newNode;
		++size;
	}
	
	// 弹出栈顶节点
	public int pop() {
		Node first = head.next;
		if(isEmpty())
			throw new IndexOutOfBoundsException("栈空");
		--size;
		head.next = first.next;
		return first.val;
	}
	
	// 查看栈顶节点的值
	public int peek() {
		if(isEmpty())
			throw new IndexOutOfBoundsException("栈空");
		return head.next.val;
	}
	
	public int size() { return this.size; }

	@Override
	public String toString() {
		if (isEmpty())
			return "空栈";
		StringBuilder sb = new StringBuilder("[");
		Node tmp = head.next;
		while (null != tmp.next) {
			sb.append(tmp.val).append(" -> ");
			tmp = tmp.next;
		}
		sb.append(tmp.val).append(']');
		return sb.toString();
	}

	public static void main(String[] args) {
		LinkedStack stack = new LinkedStack();
		stack.push(1);
		stack.push(2);
		stack.push(3);
		System.out.println(stack.pop());
		System.out.println(stack.peek());
		System.out.println(stack);
		System.out.println(stack.size());
	}
	
}
