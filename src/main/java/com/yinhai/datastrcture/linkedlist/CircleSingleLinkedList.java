package com.yinhai.datastrcture.linkedlist;

import java.util.stream.IntStream;

/**
 * @Author: yangzl
 * @Date: 2019/11/9 16:42
 * @Desc: .. 单向环形链表，需要一个指向第一个节点的指针，类似于单链表的head节点
 * 		遍历：辅助指针，当tmp.next = first时遍历结束
 * 		解决约瑟夫问题：	
 **/
public class CircleSingleLinkedList {

	Node first;
	int size;
	public CircleSingleLinkedList() { this.first = new Node(-1); }

	/*
	 * 数数并出圈，这里未rangeCheck(start, step)
	 * start：第几个开始
	 * step： 步长 > 1
	 */
	public void countAndExit(int start, int step) {

		Node tmp = first;
		// prev指向tmp的前一个
		Node prev = first;
		while (prev.next != first)
			prev = prev.next;
		// 找到出发位置
		for(int i = 0; i < start - 1; ++i) {
			tmp = tmp.next;
			prev = prev.next;
		}
		// 开始数数并出圈
		while (this.size != 1) {
			for (int x = 0; x < step - 1; ++x) {
				tmp = tmp.next;
				prev = prev.next;
			}
			System.out.print(tmp.val+"出圈 ");
			// 继续从下一个开始数
			tmp = tmp.next;
			prev.next = tmp;
			this.size--;
		}
	}

	/* 初始化约瑟夫环 */
	public void initialJosepfu(int num) {
		IntStream.rangeClosed(1, num).forEach(this::add);
	}
	
	/* 添加，第一个元素要把被first引用 */
	private void add(int val) {
		Node node = new Node(val);
		if(null == first.next) {
			first = node;
			first.next = first;
		} else {
			Node tmp = first;
			while (tmp.next != first) {
				tmp = tmp.next;
			}
			tmp.next = node;
			node.next = first;
		}
		size++;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Node tmp = first;
		while (tmp.next != first) {
			sb.append(tmp.val).append(" -> ");
			tmp = tmp.next;
		}
		Object o = null == first.next ? sb.append("空链表") : sb.append(tmp.val);
		return sb.toString();
	}

	public static void main(String[] args) {
		CircleSingleLinkedList list = new CircleSingleLinkedList();
		list.initialJosepfu(5);
		System.out.println(list);
		list.countAndExit(1, 2);
	}
}
