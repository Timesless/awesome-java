package com.yangzl.datastrcture.tree.huffman;

/**
 * @Author: yangzl
 * @Date: 2019/11/17 15:44
 * @Desc: .. 构建哈夫曼树的节点类
 **/
public class Node implements Comparable{

	Character val;	// 值
	int weight;	// 权重
	Node left, right;

	public Node(Character val, int weight) {
		this.val = val;
		this.weight = weight;
	}

	@Override
	public int compareTo(Object o) {
		Node oth = (Node) o;
		return this.weight - oth.weight;
	}

	@Override
	public String toString() {
		return "[val = " + val + ", weight = " + weight + "]";
	}
}
