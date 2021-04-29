package com.yangzl.datastructure.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yangzl
 * @date 2019/11/17 15:46
 * 
 * 	哈夫曼树： 又称最优二叉树， 树的带权路径长度最小（所有叶子节点带权路径长度总和最小）
 * 			用于构建Huffman编码。
 */
public class HuffmanTree {
	
	// Huffman树节点类，包括权重和值
	private static class Node implements Comparable<Node> {
		Character val;	// 值
		int weight;	// 权重
		Node left, right;
		public Node(Character val, int weight) {
			this.val = val;
			this.weight = weight;
		}
		@Override
		public int compareTo(Node o) {
			return this.weight - o.weight;
		}
		@Override
		public String toString() {
			return "[val = " + val + ", weight = " + weight + "]";
		}
	}
	
	/**
	 * @date 2019/11/17
	 * @desc 前序遍历
	 */
	public static void createHuffman(char[] arr) {
		List<Node> list = getNodeList(arr);
		Node root = huffmanTree(list);
		preOrder(root);
	}

	private static void preOrder(Node node) {
		System.out.println(node);
		if (null != node.left) preOrder(node.left);
		if (null != node.right) preOrder(node.right);
	}

	/**
	 * @date 2019/11/17
	 * @desc 将数组的元素构建为huffmanTree，返回哈夫曼树的根节点
	 */
	public static Node huffmanTree(List<Node> list) {
		
		Node left, right, parent;
		while (list.size() > 1) {
			Collections.sort(list);
			// 取出权重最小的两个节点生成新节点，并加入到集合中，依次重复
			left = list.remove(0);
			right = list.remove(0);
			// 这里非叶子节点的数据值可存为null
			parent = new Node(null, left.weight + right.weight);
			parent.left = left;
			parent.right = right;
			list.add(parent);
		}
		return list.get(0);
	}
	
	private static List<Node> getNodeList(char[] arr) {
		Map<Character, Integer> map = new HashMap<>(arr.length << 1);
		// 统计每个字母出现的频次
		for (char tmp: arr) {
			// 如果tmp不存在，存入指定值1，如果存在合并oldValue 与 指定值
			// map.merge(tmp, 1, (oldVal, newVal) -> oldVal + newVal);
			map.merge(tmp, 1, Integer::sum);
		}
		List<Node> result = new ArrayList<>(arr.length);
		map.forEach((k, v) -> result.add(new Node(k, v)));
		return result;
	}


	/** 测试 */
	public static void main(String[] args) {
		char[] arr = "i like like buzz fizz clazz foo bar champion".toCharArray();
		HuffmanTree.createHuffman(arr);
	}
}
