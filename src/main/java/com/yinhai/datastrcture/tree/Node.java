package com.yinhai.datastrcture.tree;

/**
 * @Author: yangzl
 * @Date: 2019/11/12 22:35
 * @Desc: .. 树节点类
 **/
public class Node {
	
	int val;
	Node left, right;
	
	public Node(int val) { this.val = val; }

	/*
	 * @Date: 2019/11/12
	 * @Desc: 前序遍历，根左右
	 **/
	public void preOrder() {
		System.out.print(this.val + " ");
		if(null != this.left)
			this.left.preOrder();
		if(null != this.right)
			this.right.preOrder();
	}
	/*
	 * @Date: 2019/11/12
	 * @Desc: 中序遍历，左根右
	 **/
	public void infixOrder() {
		if(null != this.left)
			this.left.infixOrder();
		System.out.print(this.val + " ");
		if(null != this.right)
			this.right.infixOrder();
	}
	/*
	 * @Date: 2019/11/12
	 * @Desc: hou序遍历，左右根
	 **/
	public void postOrder() {
		if(null != this.left)
			this.left.postOrder();
		if(null != this.right)
			this.right.postOrder();
		System.out.print(this.val + " ");
	}
	
	
	/**
	 * @Date: 2019/11/12
	 * @Desc: 前中后序查找
	 * 以前序为例： 判断当前节点值 == 查找值
	 * 			定义变量接收左子树查找的值，如果找到，可以提前返回，没找到不能返回，应该继续右子树查找
	 * 			在右子树的查找到 | 未查找到都直接返回 return this.right.preSearch(num)
	 * 			统计次数一定要写在比较之前，因为不管递归多少次，总是在这里比较
	 * 			写在方法开始的位置是不准确的，因为有可能这个节点只是进入，并不进行比较。
	 * 				例如： 中序查找， 父节点只是进入方法，并不执行比较，是先执行的父节点的左子树去比较的	
	 **/
	public int preSearch(int num) {
		System.out.println("进入前序查找...");
		if(this.val == num)
			return this.val;
		int result = -1;
		if(null != this.left)
			result = this.left.preSearch(num);
		if(result != -1)
			return result;
		if(null != this.right)
			return this.right.preSearch(num);
		return result;
	}
	public int infixSearch(int num) {
		int result = -1;
		if(null != this.left)
			result =  this.left.infixSearch(num);
		if(result != -1)
			return result;
		System.out.println("进入中序查找...");
		if(this.val == num)
			return this.val;
		if(null != this.right)
			return this.right.infixSearch(num);
		return result;
	}
	public int postSearch(int num) {
		int result = -1;
		if(null != this.left)
			result = this.left.postSearch(num);
		if(result != -1)
			return result;
		if(null != this.right)
			result = this.right.postSearch(num);
		if(result != -1)
			return result;
		System.out.println("进入后序查找...");
		if(this.val == num)
			return this.val;
		return -1;
	}
	
}
