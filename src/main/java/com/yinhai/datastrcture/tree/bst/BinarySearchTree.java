package com.yinhai.datastrcture.tree.bst;

import com.yinhai.datastrcture.tree.BinaryTree;

/**
 * @Author: yangzl
 * @Date: 2019/12/3 20:09
 * @Desc: .. 二叉搜索树 | 二叉排序树
 **/
public class BinarySearchTree {
	
	private Node root;
	
	public BinarySearchTree() {}
	public BinarySearchTree(Node root) { this.root = root; }
	
	/**
	 * @Date: 2019/12/3 前中后序遍历
	 * @Desc: 
	 **/
	public void preOrder() {
		if (null == root) { return; }
		preOrder(this.root);
	}
	public void infixOrder() {
		if (null == root) { return; }
		infixOrder(this.root);
	}
	public void postOrder() {
		if (null == root) { return; }
		postOrder(this.root);
	}
	/**
	 * @Date: 2019/12/3 重载提供实现
	 * @Desc:  这里直接使用的node.val就没有使用Node类的toString()
	 **/
	private void preOrder(Node node) {
		System.out.print(node.val + " ");
		if (null != node.left) { preOrder(node.left); }
		if (null != node.right) { preOrder(node.right); }
	}
	private void infixOrder(Node node) {
		if (null != node.left) { infixOrder(node.left); }
		System.out.print(node.val + " ");
		if (null != node.right) { infixOrder(node.right); }
	}
	private void postOrder(Node node) {
		if (null != node.left) { postOrder(node.left); }
		if (null != node.right) { postOrder(node.right); }
		System.out.print(node.val + " ");
	}
	
	
	/**
	 * @Date: 2019/12/3 添加节点
	 * @Desc: 
	 **/
	public void add(int val) { root = add(val, root); }
	// 重载提供实现
	private Node add(int val, Node node) {
		if (null == node) {
			return new Node(val);
		}
		if (val < node.val) {
			node.left = add(val, node.left);
		} else if (val > node.val){
			node.right = add(val, node.right);
		} else 
			;	// duplicate; do nothing
		return node;
	}	
	
	

}
