package com.yangzl.datastructure;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

/**
 * @Author: yangzl
 * @Date: 2020/2/11 15:31
 * @Desc: .. 二分搜索树，增删查时间复杂度为O(h)
 * 	递归
 * 		1 判断基准情形
 * 		2 向基准情形不断推进
 * 	二分搜索树的扩展
 * 		floor/ceil
 * 		successor/predecessor
 * 		rank/select
 * 		可重复 count
 * 	AVL树
 * 		在insert, remove之后添加平衡例程即可。@see com.yangzl.datastrcture.tree.avl.AvlTree
 */
public class BST<E extends Comparable<? super E>> {

	private Node<E> root;
	private int size;
	public BST() {}
	public BST(Node<E> root) {
		this.root = root;
		size = 1;
	}
	
	/*
	 * 内部类，持有外部类的引用
	 * 静态嵌套类，是一个独立的类，完全是形式上的“内部”，和外部类神似
	 */
	private static class Node<E> {
		E val;
		Node<E> left, right;
		public Node(E val) { this.val = val; }
	}

	public int size() { return this.size; }
	public boolean isEmpty() { return this.size == 0; }
	/**
	 * @Date: 2019/12/3 前中后序遍历，是DFS
	 * @Desc: 以下的判断可以在递归的例程进行判断，这样就无须判断多次
	 * 		对每个节点有三次机会进行访问，所以前中后遍历只是选择某次机会进行访问
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
	 * @date 2020/2/11
	 * @desc 前序遍历的非递归实现，使用栈记录将要依次访问的节点
	 */
	public void preOrderNR() {
		if (null == root)
			return;
		Deque<Node<E>> stack = new ArrayDeque<>(this.size);
		stack.push(root);
		while (!stack.isEmpty()) {
			Node<E> top = stack.pop();
			System.out.print(top.val + " ");
			// 先压入右子树，再压入左子树
			if (null != top.right) 
				stack.push(top.right);
			if (null != top.left)
				stack.push(top.left);
		}
	}
	
	/**
	 * @Date: 2020/2/11
	 * @Desc: 中序遍历非递归实现
	 */
	public void inOrderNR() {
		if (null == root)
			return;
		Deque<Node<E>> stack = new ArrayDeque<>(this.size);
		Node<E> p = root;
		while (!stack.isEmpty() || p != null) {
			while (p != null) {
				stack.push(p);
				p = p.left;
			}
			p = stack.pop();
			System.out.print(p.val + " ");
			p = p.right;
		}
	}
	
	/**
	 * @Date: 2020/2/11
	 * @Desc:  广度优先遍历，借助队列实现
	 */
	public void BFS() {
		Queue<Node<E>> queue = new ArrayDeque<>(this.size);
		queue.offer(root);
		while (!queue.isEmpty()) {
			Node<E> front = queue.poll();
			System.out.print(front.val + " ");
			if (null != front.left)
				queue.offer(front.left);
			if (null != front.right)
				queue.offer(front.right);
		}
	}

	/**
	 * @Date: 2019/12/3 添加节点
	 * @Desc: 重载提供实现
	 */
	public void add(E val) { root = this.add(val, root); }
	
	/**
	 * @Date: 2020/2/11
	 * @Desc: 是否包含元素
	 */
	public boolean contains(E e) { return this.contains(root, e); }
	
	public E findMin() {
		if (isEmpty()) throw new IndexOutOfBoundsException("树为空");
		return minimun(root).val;
	}
	public E findMax() {
		if (isEmpty()) throw new IndexOutOfBoundsException("树为空");
		return maximum(root).val;
	}
	
	/**
	 * @Date: 2020/2/11 删除节点
	 * @Desc: 只有一个子树时，用子树替代删除节点
	 * 		左右子树都存在时，用左子树的最大节点或右子树的最小节点代替删除节点
	 */
	public void remove(E e) { root = remove(root, e); }
	
	// ======================================================================
	// divide line
	// ======================================================================

	/**
	 * @Date: 2019/12/3 重载提供实现
	 * @Desc: 这样判断可节约栈帧
	 */
	private void preOrder(Node<E> node) {
		System.out.print(node.val + " ");
		if (null != node.left) { preOrder(node.left); }
		if (null != node.right) { preOrder(node.right); }
	}
	private void infixOrder(Node<E> node) {
		if (null != node.left) { infixOrder(node.left); }
		System.out.print(node.val + " ");
		if (null != node.right) { infixOrder(node.right); }
	}
	private void postOrder(Node<E> node) {
		if (null != node.left) { postOrder(node.left); }
		if (null != node.right) { postOrder(node.right); }
		System.out.print(node.val + " ");
	}

	private Node<E> add(E val, Node<E> node) {
		if (null == node) {
			// 只有执行到这里才会添加元素
			size++;
			return new Node<>(val);
		}
		// 判断在哪个位置执行插入
		int cmpResult = val.compareTo(node.val);
		if (cmpResult < 0) {
			node.left = add(val, node.left);
		} else if (cmpResult > 0){
			node.right = add(val, node.right);
		} else
			;	// duplicate; do nothing
		return node;
		// return banlance(node);
	}

	private boolean contains(Node<E> node, E e) {
		if (null == node)
			return false;
		int cmpResult = e.compareTo(node.val);
		if (cmpResult == 0)
			return true;
		else if (cmpResult < 0)
			return contains(node.left, e);
		else
			return contains(node.right, e);
	}

	/**
	 * @Date: 2020/2/11
	 * @Desc: 当前节点作为根节点上最小值的节点
	 */
	private Node<E> minimun(Node<E> node) {
		while (null != node)
			node = node.left;
		return node;
	}
	// 当前节点作为根节点的最大值的节点。递归实现
	private Node<E> maximum(Node<E> node) {
		if (null == node || null == node.right)
			return node;
		return maximum(node.right);
	}

	private Node<E> remove(Node<E> node, E e) {
		if (null == node)
			return null;
		int cmpResult = e.compareTo(node.val);
		if (cmpResult < 0) 
			node.left = remove(node.left, e);
		else if (cmpResult > 0)
			node.right = remove(node.right, e);
		else {
			/*
			 * 左右子树都存在
			 * 右子树的最小节点，后继
			 * 左子树的最大节点，前驱
			 * 	E predecessorVal = maximum(node.left).val;
			 *	node.left = remove(node.left, predecessorVal);
			 */
			if (null != node.left && null != node.right) {
				// 最小节点的值赋值回node
				node.val = minimun(node.right).val;
				node.right = remove(node.right, node.val);
			} else {
				node = null != node.left ? node.left : node.right;
				// 只有这里实际执行了删除
				size--;
			}
		}
		return node;
		// return banlance(node);
	}
	

	public static void main(String[] args) {
		BST<Integer> bst = new BST<>();
		bst.add(6);
		bst.add(8);
		bst.add(2);
		bst.add(1);
		bst.add(4);
		bst.add(3);
		bst.remove(2);
		System.out.println(bst.size());
		bst.inOrderNR();
	}
}
