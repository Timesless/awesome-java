package com.yangzl.datastrcture.tree;

import org.junit.Test;

/**
 * @Author: yangzl
 * @Date: 2019/12/3 22:20
 * @Desc: ..
 */
public class AvlTest {

	/**
	 * @Date: 2019/12/3
	 * @Desc: 左旋测试
	 */
	@Test
	public void leftRotate() {
		int[] ary = {4, 3, 6, 5, 7, 8};
		AvlTree tree = new AvlTree();
		for (int tmp : ary) { tree.add(tmp); }
		System.out.println(tree.height());
		tree.infixOrder();	// 3 4 5 6 7 8 
	}
	
	/**
	 * @Date: 2019/12/3
	 * @Desc: 右旋测试
	 */
	@Test
	public void rightRotate() {
		int[] ary = {10, 12, 8, 9, 7, 6};
		AvlTree tree = new AvlTree();
		for (int tmp : ary) { tree.add(tmp); }
		System.out.println(tree.height());
		tree.infixOrder();
	}
	
	/**
	 * @Date: 2019/12/3
	 * @Desc: 左右双旋，注意左旋是指当前节点即k3的左子树即k1左旋，右旋是当前节点的树右旋
	 */
	@Test
	public void leftRightRotate() {
		int[] ary = {10, 11, 7, 6, 8, 9};
		AvlTree tree = new AvlTree();
		for (int tmp : ary) { tree.add(tmp); }
		System.out.println(tree.height());
		tree.infixOrder();
	}

	/**
	 * @Date: 2019/12/3
	 * @Desc: 右左双旋，注意右旋是指当前节点即k3的右子树即k2左旋，左旋是当前节点的树左旋
	 */
	@Test
	public void rightLeftRotate() {
		int[] ary = {2, 1, 6, 5, 7, 3};
		AvlTree tree = new AvlTree();
		for (int tmp : ary) { tree.add(tmp); }
		System.out.println(tree.height());
		tree.infixOrder();
	}
	
}
