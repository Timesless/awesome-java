package com.yangzl;

/**
 * @author yangzl
 * @date 2020/4/30
 *
 * LeetCode链表类
 */
public class ListNode {
	int val;
	ListNode next;
	public ListNode(int val) { this.val = val; }
	public int getVal() { return val; }

	@Override
	public String toString() {
		StringBuilder bd = new StringBuilder(16);
		ListNode p = this;
		while (p.next != null) {
			bd.append(p.val).append(" -> ");
			p = p.next;
		}
		bd.append(p.val);
		return bd.toString();
	}
	
	/**
	 * 2020/11/29 头插
	 * 
	 * @param val 新增节点值
	 * @return ListNode
	 */
	public ListNode addHead(int val) {
		ListNode dummy = new ListNode(-1);
		dummy.next = this;
		// dummy new this
		ListNode newNode = new ListNode(val);
		newNode.next = dummy.next;
		dummy.next = newNode;
		return newNode;
	}
	
	/**
	 * 2020/11/29 尾插
	 * 
	 * @param val val
	 * @return ListNode
	 */
	public ListNode addTail(int val) {
		// this next new
		ListNode p = this;
		while (p.next != null) {
			p = p.next;
		}
		p.next = new ListNode(val);
		return this;
	}
	
	
}
