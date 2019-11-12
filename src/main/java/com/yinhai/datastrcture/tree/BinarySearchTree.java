package com.yinhai.datastrcture.tree;

/**
 * @Author: yangzl
 * @Date: 2019/11/12 22:38
 * @Desc: .. 二叉排序树 | 二叉搜索树 BST
 **/
public class BinarySearchTree {

	// 头节点引用
	private Node head;

	public BinarySearchTree(Node head) { this.head = head; }

	/*
	 * @Date: 2019/11/12
	 * @Desc: 前中后序遍历
	 **/
	public void preOrder() {
		if(null == head)
			throw new RuntimeException("空二叉树");
		head.preOrder();
	}
	public void infixOrder() {
		if(null == head)
			throw new RuntimeException("空二叉树");
		head.infixOrder();
	}
	public void postOrder() {
		if(null == head)
			throw new RuntimeException("空二叉树");
		head.postOrder();
	}

	/*
	 * @Date: 2019/11/12
	 * @Desc: 前中后序查找，左右根
	 **/
	public int preSearch(int num) {
		if(null == head)
			throw new RuntimeException("空二叉树");
		return this.head.preSearch(num);
	}
	public int infixSearch(int num) {
		if(null == head)
			throw new RuntimeException("空二叉树");
		return this.head.infixSearch(num);
	}
	public int postSearch(int num) {
		if(null == head)
			throw new RuntimeException("空二叉树");
		return this.head.postSearch(num);
	}
	
	
	/**
	 * @Date: 2019/11/13
	 * @Desc: 节点的删除，一定是判断当前节点的子节点是否需要删除，因为这里树是单向的
	 **/
	
}
