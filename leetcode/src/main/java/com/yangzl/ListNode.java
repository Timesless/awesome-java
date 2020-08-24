package com.yangzl;

/**
 * @Date: 2020/4/30
 * @Desc: LeetCode链表类
 */
public class ListNode {
	int val;
	ListNode next;

	public ListNode(int val) { this.val = val; }
	
	public int getVal() { return val; }

	@Override
	public String toString() {
		StringBuilder bd = new StringBuilder();
		ListNode p = this;
		while (p.next != null) {
			bd.append(p.val + " -> ");
			p = p.next;
		}
		bd.append(p.val);
		return bd.toString();
	}
	
	// TODO
}