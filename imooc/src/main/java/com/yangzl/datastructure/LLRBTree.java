package com.yangzl.datastructure;

/**
 * @Author: yangzl
 * @Date: 2020/2/15 20:51
 * @Desc: .. 左倾红黑树 == 2-3树
 * MaxHeight = 2logN，当所有节点都是3节点，那么会多logN的红节点
 * ..右倾红黑树
 * ..删除例程
 * ..伸展树（刚被访问的数据很可能再被访问）：find顺便向上压缩高度
 */
public class LLRBTree<K extends Comparable<? super K>,V> {
	private static final boolean RED = true;
	private static final boolean BLACK = false;
	
	static class Node<K extends Comparable<? super K>,V> {
		K k;
		V v;
		Node<K,V> left, right;
		boolean color;
		public Node(K key, V val) {
			this.k = key;
			this.v = val;
			color = RED;
		}
	}
	
	private Node<K,V> root;
	private int size;
	public LLRBTree() {}
	
	public int size() { return this.size; }
	/**
	 * @Date: 2020/2/15
	 * @Desc: 
	 */
	public void add(K k, V v) {
		root = add0(root, k, v);
		// 维持根节点为黑色
		root.color = BLACK;
	}
	private Node<K, V> add0(Node<K, V> node, K k, V v) {
		if (null == node) {
			size++;
			return new Node<>(k, v);
		}
		int cmpResult = k.compareTo(node.k);
		if (cmpResult < 0)
			node.left = add0(node.left, k, v);
		else if (cmpResult > 0)
			node.right = add0(node.right, k, v);
		else
			node.v = v;	//cover
		// 回溯维护黑节点的平衡
		return balanceBlack(node);
	}

	/**
	 * @Date: 2020/2/15
	 * @Desc: 所有步骤如下：维持黑节点平衡
	 *  BLACK 	BLACK    BLACK	  BLACK         RED
	 *   /	 	/         /	   	   / \          / \
	 * RED ==> RED  ==> RED ==>  RED RED ==> BLACK BLACK
	 * 	1 	 	\		/                        1
	 * 	1  		RED    RED                       1
	 * 	1  	            1	                     1
	 *  ------------------------------------------
	 */
	private Node<K,V> balanceBlack(Node<K,V> node) {
		// 左旋
		if (!isRed(node.left) && isRed(node.right))
			node = leftRotate(node);
		// 右旋
		if (isRed(node.left) && isRed(node.left.left)) 
			node = rightRotate(node);
		// 翻转颜色
		if (isRed(node.left) && isRed(node.right))
			flipColor(node);
		return node;
	}

	private boolean isRed(Node<K,V> node) { return null == node ? BLACK : node.color; }
	
	/**
	 * @Date: 2020/2/15
	 * @Desc:  右旋转，不维持红黑树性质
	 *         BLACK         RED                BLACK
	 *          /           /  \     change     /  \
	 *        RED    =>   RED  BLACK  ====>    RED  RED
	 *        /
	 *      RED
	 */
	private Node<K,V> rightRotate(Node<K,V> k2) {
		Node<K,V> k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		// changeColor，执行完需判断是否flipColor
		k1.color = k2.color;
		k2.color = RED;
		return k1;
	}

	/**
	 * @Date: 2020/2/15
	 * @Desc: 左旋转，不维持红黑树性质
	 *    k1
	 *      \
	 *       k2
	 */
	private Node<K,V> leftRotate(Node<K, V> k1) {
		Node<K,V> k2 = k1.right;
		k1.right = k2.left;
		k2.left = k1;
		// 执行完毕需判断是否flipColor
		k2.color = k1.color;
		k1.color = RED;
		return k2;
	}
	
	/**
	 * @Date: 2020/2/15
	 * @Desc: 颜色翻转
	 */
	private void flipColor(Node<K,V> node) {
		node.left.color = node.right.color = BLACK;
		node.color = RED;
	}

	public void printInOrder() { printInOrder(root); }
	private void printInOrder(Node<K,V> node) {
		if (null != node.left)
			printInOrder(node.left);
		if (node.color)
			System.out.printf("%s = %s, color = red\n", node.k, node.v);
		else
			System.out.printf("%s = %s, color = black\n", node.k, node.v);
		if (null != node.right)
			printInOrder(node.right);
	}


	public static void main(String[] args) {
		LLRBTree<Integer, Object> tree = new LLRBTree<>();
		tree.add(42, null);
		tree.add(32, null);
		tree.add(40, null);
		tree.add(24, null);
		tree.add(28, null);
		tree.printInOrder();
	}
}
