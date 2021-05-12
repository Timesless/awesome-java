package com.yangzl.datastructure.tree;

/**
 * @author yangzl
 * @date 2019/12/3 20:09
 *
 * 二叉搜索树 | 二叉排序树，正式版本可作为参考
 */
public class BinarySearchTree {
	
	private Node root;
	
	public BinarySearchTree() {}
	public BinarySearchTree(Node root) { this.root = root; }
	
	private static class Node {
		int val;
		Node left, right;
		public Node(int val) { this.val = val; }

	}
	
	/**
	 * @date 2019/12/3 前中后序遍历
	 * @desc
	 */
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
	 * @date 2019/12/3 重载提供实现
	 * @desc 这里直接使用的node.val就没有使用Node类的toString()
	 */
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
	 * @date 2019/12/3 添加节点
	 */
	public void add(int val) { root = add(val, root); }
	// 重载提供实现
	private Node add(int val, Node node) {
		if (null == node) { return new Node(val); }
		if (val < node.val) {
			node.left = add(val, node.left);
		} else if (val > node.val){
			node.right = add(val, node.right);
		} else 
			;	// duplicate; do nothing
		return node;
	}


	public static void main(String[] args) {
		BinarySearchTree bst = new BinarySearchTree();
		int[] ary = {7, 3, 10, 12, 5, 1, 9};
		for (int tmp : ary) { bst.add(tmp); }
		bst.infixOrder();
	}
}
