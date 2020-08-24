package com.yangzl.datastructure.linked;

/**
 * @Author: yangzl
 * @Date: 2020/2/11 13:51
 * @Desc: .. 切勿犯“完美主义”的错误
 */
public class LinkedNode<E> {
	private Node<E> dummyHead;
	private int size;
	public LinkedNode() { dummyHead = new Node<>(null, null); }
	
	public void addLast() { this.add(size, dummyHead); }
	public void addFirst() { this.add(0, dummyHead); }

	private void add(int idx, Node<E> node) {
		
	}
}
