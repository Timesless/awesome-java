package com.yangzl.datastructure.linked;

/**
 * @author yangzl
 * @date 2020/2/11 13:51
 *
 * 切勿犯“完美主义”的错误
 * 
 * 		TODO
 */
public class LinkedDeque<E> {
	
	private final Node<E> dummyHead;
	private int size;
	public LinkedDeque() { dummyHead = new Node<>(null, null); }
	
	public void addLast() { this.add(size, dummyHead); }
	public void addFirst() { this.add(0, dummyHead); }

	private void add(int idx, Node<E> node) {
		
	}
	
	
}
