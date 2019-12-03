package com.yinhai.datastrcture.tree.bst;

/**
 * @Author: yangzl
 * @Date: 2019/12/3 20:07
 * @Desc: ..
 **/
public class Node {

	int val;
	Node left, right;
	
	public Node(int val) {
		this.val = val;
	}

	@Override
	public String toString() {
		return String.valueOf(this.val);
	}
}
