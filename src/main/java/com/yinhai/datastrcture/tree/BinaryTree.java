package com.yinhai.datastrcture.tree;

/**
 * @Author: yangzl
 * @Date: 2019/11/12 22:38
 * @Desc: .. 二叉树，只实现遍历和查找功能，其它功能参见bst/BinarySearchTree
 **/
public class BinaryTree {

	// 头节点引用
	private Node root;

	public BinaryTree(Node root) { this.root = root; }
	
	public void sufixOrder() {
		sufixOrder(root);
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
		if(null == root)
			throw new RuntimeException("空二叉树");
		root.preOrder();
	}
	public void infixOrder() {
		if(null == root)
			throw new RuntimeException("空二叉树");
		root.infixOrder();
	}
	public void postOrder() {
		if(null == root)
			throw new RuntimeException("空二叉树");
		root.postOrder();
	}

	/*
	 * @Date: 2019/11/12
	 * @Desc: 前中后序查找，左右根
	 **/
	public int preSearch(int num) {
		if(null == root)
			throw new RuntimeException("空二叉树");
		return this.root.preSearch(num);
	}
	public int infixSearch(int num) {
		if(null == root)
			throw new RuntimeException("空二叉树");
		return this.root.infixSearch(num);
	}
	public int postSearch(int num) {
		if(null == root)
			throw new RuntimeException("空二叉树");
		return this.root.postSearch(num);
	}
	
}
