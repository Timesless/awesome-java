package com.yinhai.datastrcture.tree.huffman;

import java.util.*;

/**
 * @Author: yangzl
 * @Date: 2019/11/17 15:46
 * @Desc: ..	哈夫曼树： 又称最优二叉树， 树的带权路径长度最小（所有叶子节点带权路径长度总和最小）
 * 			用于构建Huffman编码。
 **/
public class HuffmanTree {
	
	/**
	 * @Date: 2019/11/17
	 * @Desc: 前序遍历
	 **/
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
	 * @Date: 2019/11/17
	 * @Desc: 将数组的元素构建为huffmanTree，返回哈夫曼树的根节点
	 **/
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
			Object obj = map.containsKey(tmp) ? map.put(tmp, map.get(tmp) + 1) : map.put(tmp, 1);
		}
		List<Node> result = new ArrayList<>(arr.length);
		map.entrySet().forEach(entry -> result.add(new Node(entry.getKey(), entry.getValue())));
		return result;
	}

}
