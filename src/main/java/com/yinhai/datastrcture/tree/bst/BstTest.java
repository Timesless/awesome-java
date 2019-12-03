package com.yinhai.datastrcture.tree.bst;

import org.junit.Test;

/**
 * @Author: yangzl
 * @Date: 2019/12/3 20:51
 * @Desc: ..
 **/
public class BstTest {
	
	/**
	 * @Date: 2019/12/3 创建二叉搜索树，并使用中序遍历，那么可得到有序序列
	 * @Desc: 
	 **/
	@Test
	public void createBST() {
		BinarySearchTree bst = new BinarySearchTree();
		int[] ary = {7, 3, 10, 12, 5, 1, 9};
		for (int tmp : ary) { bst.add(tmp); }
		bst.infixOrder();
	}

}
