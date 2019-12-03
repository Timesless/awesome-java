package com.yinhai.datastrcture.tree;

import org.junit.Test;

/**
 * @Author: yangzl
 * @Date: 2019/11/12 22:51
 * @Desc: ..	满二叉树：树的节点个数为2的n次方，n为树的高度。
 * 				完全二叉树：在满二叉树的基础上从右往左顺序删除部分节点。
 **/
public class TreeTest {

	/*
	 * @Date: 2019/11/12
	 * @Desc: 树初始化
	 **/
	public Node init() {
		Node head = new Node(1);
		Node n1 = new Node(2);
		Node n2 = new Node(3);
		Node n3 = new Node(4);
		Node n4 = new Node(5);
		head.left = n1;
		head.right = n2;
		n2.left = n4;
		n2.right = n3;
		return head;
	}
	
	/*
	 * @Date: 2019/11/12
	 * @Desc: 测试二叉树前中后序遍历
	 **/
	@Test
	public void testTreeTraversing() {
		Node head = this.init();
		head.preOrder();	// 12354
		System.out.println();
		head.infixOrder();	// 21534
		System.out.println();
		head.postOrder(); // 25431
	}

	/*
	 * @Date: 2019/11/12
	 * @Desc: 测试二叉树前中后序查找
	 **/
	@Test
	public void testTreeSearch() {
		Node head = this.init();
		System.out.println(head.preSearch(3));
		System.out.println(head.infixSearch(3));
		System.out.println(head.postSearch(3));
	}


	@Test
	public void testTreeSearch2() {
		Node head = this.init();
		BinaryTree tree = new BinaryTree(head);
		tree.sufixOrder();
	}
	
	

}
