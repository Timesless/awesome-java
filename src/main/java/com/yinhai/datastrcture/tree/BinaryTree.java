package com.yinhai.datastrcture.tree;

/**
 * @Author: yangzl
 * @Date: 2019/11/12 22:38
 * @Desc: .. 二叉排序树 | 二叉搜索树 BST
 **/
public class BinaryTree {

	// 头节点引用
	private Node head;

	public BinaryTree(Node head) { this.head = head; }
	
	
	public void sufixOrder() {
		sufixOrder(head);
	}

	/**
	 * @Date: 2019/11/16
	 * @Desc: 感觉应该用这种方式， node需要高内聚，不需要实现遍历或者查找这种类型的代码，应该在tree中实现
	 **/
	private void sufixOrder(Node node) {
		if (null != node.left) sufixOrder(node.left);
		if (null != node.right) sufixOrder(node.right);
		System.out.print(node.val + " ");
	}


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
