package com.imooc.datastructure.linked;

/**
 * @Author: yangzl
 * @Date: 2020/2/11 14:16
 * @Desc: .. 该节点类可作为各实现类的静态内部类提供。
 */
public class Node<E> {
	E val;
	Node<E> next;
	public Node(E val, Node<E> next) {
		this.val = val;
		this.next = next;
	}
}
