package com.yangzl.datastructure;

/**
 * @author yangzl
 * @date 2020/2/11 21:51
 * @desc .. 二叉查找映射
 */
public class BSTMap<K extends Comparable<? super K>, V> implements Map<K, V> {

	private Node<K, V> root;
	private int size;
	public BSTMap() {}
	public BSTMap(Node<K, V> root) { this.root = root; }
	
	static class Node<K, V> {
		K k;
		V v;
		Node<K, V> left, right;
		public Node(K k, V v) {
			this.k = k;
			this.v = v;
		}
	}
	
	@Override
	public int size() { return size; }
	@Override
	public boolean isEmpty() { return size == 0; }
	@Override
	public boolean contains(K k) { return getNode(root, k) != null; }

	/**
	 * 添加键值对「k，v」
	 *
	 * @param k key
	 * @param v value
	 */
	@Override
	public void add(K k, V v) { root = add(root, k, v); }

	// 删除对应的k
	@Override
	public V remove(K k) {
		if (isEmpty()) throw new IllegalArgumentException("空");
		V result = this.get(k);
		if (null != result) { root = remove(root, k); }
		return result;
	}
	
	// 获取k对应的v
	@Override
	public V get(K k) {
		Node<K, V> target = getNode(root, k);
		return null == target ? null : target.v;
	}

	// 不存在则不更新
	@Override
	public void set(K k, V newV) {
		Node<K, V> target = getNode(root, k);
		if (null != target)
			target.v = newV;
	}
	
	public void print() { print(root); }
	
	// ========================================================================
	// divide line
	// ========================================================================
	
	// 获取k对应的节点
	private Node<K, V> getNode(Node<K, V> node, K k) {
		if (null == node)
			return null;
		int cmpResult = k.compareTo(node.k);
		if (cmpResult < 0)
			return getNode(node.left, k);
		else if (cmpResult > 0)
			return getNode(node.right, k);
		else 
			return node;
	}

	// 添加节点
	private Node<K, V> add(Node<K, V> node, K k, V v) {
		if (null == node) {
			size++;
			return new Node<>(k, v);
		}
		int cmpResult = k.compareTo(node.k);
		if (cmpResult < 0)
			node.left = add(node.left, k, v);
		else if (cmpResult > 0)
			node.right = add(node.right, k, v);
		else	// duplicate
			node.v = v;
		return node;
	}

	// 删除节点
	private Node<K, V> remove(Node<K, V> node, K k) {
		int cmpResult = k.compareTo(node.k);
		if (cmpResult < 0)
			node.left = remove(node.left, k);
		else if (cmpResult > 0)
			node.right = remove(node.right, k);
		else {
			// 执行删除
			if (null != node.left && null != node.right) {
				Node<K, V> predecessor = maximum(node.left);
				node.k = predecessor.k;
				node.v = predecessor.v;
				node.left = remove(node.left, predecessor.k);
			} else {
				node = null != node.left ? node.left : node.right;
				size--;
			}
		}
		return node;
	}

	// 获取以当前节点为根节点的最大值的节点
	private Node<K, V> maximum(Node<K, V> node) {
		if (null == node || node.right == null)
			return node;
		return maximum(node.right);
	}

	// 打印
	private void print(Node<K, V> node) {
		if (node.left != null)
			print(node.left);
		System.out.printf("%s = %s ", node.k, node.v);
		if (node.right != null)
			print(node.right);
	}
	
	
	public static void main(String[] args) {
		BSTMap<String, Integer> bstMap = new BSTMap<>();
		bstMap.add("d", 100);
		bstMap.add("b", 98);
		bstMap.add("a", 97);
		bstMap.add("c", 99);
		bstMap.add("h", 103);
		bstMap.print();
		System.out.println();
		System.out.println(bstMap.remove("b"));
		bstMap.print();
	}
}
