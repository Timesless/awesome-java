package com.yangzl;

import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @Author: yangzl
 * @Date: 2020/3/31 13:50
 * @Desc: ..
 */

public class Tree {

	/*
	 * Leetcode 二叉树节点类
	 */
	public static class TreeNode {
		int val;
		TreeNode left, right;
		public TreeNode() {}
		public TreeNode(int _val) { this.val = _val; }
	}

	// N叉树节点类
	private static class Node {
		int val;
		List<Node> children;

		public Node() {}
		public Node(int _val) { val = _val; }
		public Node(int _val, List<Node> _children) {
			val = _val;
			children = _children;
		}
	}
	
	/**
	 * @Date: 2020/3/31
	 * @Desc: N叉树前序遍历 -> 迭代
	 */
	public List<Integer> nPreorderIter(Node root) {
		List<Integer> rs = new ArrayList<>();
		if (root == null) return rs;
		Deque<Node> stack = new LinkedList<>();
		stack.push(root);
		while (!stack.isEmpty()) {
			Node cur = stack.pop();
			rs.add(cur.val);
			// 以二叉树为例，先压入右子树，再压入左子树，这样才会先访问左子树
			Collections.reverse(cur.children);
			for (Node tmp : cur.children)
				stack.push(tmp);
		}
		return rs;
	}
	
	/**
	 * @Date: 2020/3/31
	 * @Desc: N叉树前序遍历 -> 递归
	 */
	public List<Integer> nPreorderRecursive(Node root) {
		List<Integer> rs = new ArrayList<>();
		nPreorderRecursive(rs, root);
		return rs;
	}
	// 递归实现
	private void nPreorderRecursive(List<Integer> rs, Node node) {
		if (node == null) return;
		rs.add(node.val);
		for (Node tmp : node.children)
			nPreorderRecursive(rs, tmp);
	}
	
	/**
	 * @Date: 2020/3/31
	 * @Desc: N叉树后序遍历 -> 迭代
	 */
	public List<Integer> nPostorderIter(Node root) {
		LinkedList<Integer> rs = new LinkedList<>();
		if (root == null) return rs;
		Deque<Node> stack = new LinkedList<>();
		stack.push(root);
		while (!stack.isEmpty()) {
			Node cur = stack.pop();
			// 头插
			rs.offerFirst(cur.val);
			// 顺序遍历子树，丢入栈中
			for (Node tmp : cur.children)
				stack.push(tmp);
		}
		return rs;
	}

	
	/**
	 * @Date: 2020/4/3
	 * @Desc:  之字形层序遍历
	 */
	List<List<Integer>> rs = new ArrayList<>();
	public List<List<Integer>> levelOrder(TreeNode root) {
		if (root == null) return rs;
		Queue<TreeNode> q = new ArrayDeque<>();
		q.offer(root);
		int level = 0;
		while (!q.isEmpty()) {
			rs.add(new LinkedList<>());
			int sz = q.size();
			for (int i = 0; i < sz; ++i) {
				TreeNode cur = q.poll();
				// 双端队列实现
				LinkedList<Integer> clist = (LinkedList<Integer>) rs.get(level);
				boolean obj = (level & 1) == 1 ? clist.offerFirst(cur.val) : clist.offerLast(cur.val);
				if (cur.left != null) q.offer(cur.left);
				if (cur.right != null) q.offer(cur.right);
			}
			++ level;
		}
		return rs;
	}
	@Test
	public void testZagLevelOrder() {
		TreeNode root = new TreeNode(1);
		TreeNode two = new TreeNode(2), three = new TreeNode(3);
		root.left = two;
		root.right = three;
		two.left = new TreeNode(4);
		three.right = new TreeNode(5);
		System.out.println(levelOrder(root));
	}

	/**
	 * @Date: 2020/4/4
	 * @Desc: N叉树的高度
	 */
	public int maxDepth(Node root) {
		if (root == null) return 0;
		List<Integer> list = new ArrayList<>();
		if (root.children != null) 
			for (Node cur : root.children)
				list.add(maxDepth(cur) + 1);
		return list.isEmpty() ? 1 : list.stream().max(Integer::compare).get();
	}
	@Test
	public void testMaxDepth() {
		List<Node> children3 = new ArrayList() {{
			add(new Node(5));
			add(new Node(6));
		}};
		Node three = new Node(3, children3);
		List<Node> children = new ArrayList() {{ 
			add(three);
			add(new Node(2));
			add(new Node(4));
		}};
		Node root = new Node(1, children);
		System.out.println(maxDepth(root));
	}


	/**
	 * 2020/9/29 利用队列实现二叉树的层序遍历
	 * @param root 树根节点
	 * @return
	 */
	public List<List<Integer>> levelOrderBinary(TreeNode root) {
		if (null == root) return new ArrayList<>();
		List<List<Integer>> rs = new LinkedList<>();
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			List<Integer> list = new ArrayList<>();
			// 通过sz确保不会多弹出元素
			int sz = queue.size();
			while (sz-- > 0) {
				TreeNode cur = queue.poll();
				if (cur.left != null) {
					queue.offer(cur.left);
				}
				if (cur.right != null) {
					queue.offer(cur.right);
				}
				list.add(cur.val);
			}
			rs.add(list);
		}
		return rs;
	}
	@Test
	public void testLevelOrder() {
		// 手动构建一棵树
		TreeNode root = new TreeNode(3);
		TreeNode ftl = new TreeNode(9);
		TreeNode ftr = new TreeNode(20);
		root.left = ftl;
		root.right = ftr;

		System.out.println(levelOrderBinary(root));
	}


}
