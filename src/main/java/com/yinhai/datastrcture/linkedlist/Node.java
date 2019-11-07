package com.yinhai.datastrcture.linkedlist;

/**
 * @Author: yangzl
 * @Date: 2019/10/22 19:51
 * @Desc:
 * 	链表节点类
 **/
public class Node {

	int val;
	Node next;
	public Node(int val) {
		this.val = val;
	}

	@Override
	public String toString() {
		return String.valueOf(this.val);
	}
}
