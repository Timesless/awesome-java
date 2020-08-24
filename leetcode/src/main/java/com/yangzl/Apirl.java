package com.yangzl;

import java.util.PriorityQueue;

/**
 * @Author: yangzl
 * @Date: 2020/4/30 13:53
 * @Desc: .. 
 * 1. A LeetCode a day keeps unemployment away
 * 2. A LeetCode a day keeps confidence away.
 * 3. 一杯茶，一包烟，一道力扣做一天。一根笔，一双手，一个 Bug 敲一宿。
 * 4. 中等都受不了，天天困难，头发都没了。
 * 5. 生活不止眼前的苟且，还有前女友送来的请帖。你赤手空拳来到这世界，为设计 twitter 不顾一切。
 * 6. 每天起床第一句，先去力扣看看题；每次看每日一题，感觉自己要智熄；自己思考两小时，最后从题解 copy；打卡，我要打卡，领完积分骗自己 - 燃烧我的力扣题。
 * 7. 一眼看出是 dp，不就背包问题，别问，问就是经验，从未失手。但也每次都在状态定义和转移上愣神，我就是这么一个迷人的，废物。
 * 8. 你明白很多道理，依然过不好这一生，你知道动态规划，但还是不知道这道题它怎么就动起来了？还自我规划了？
 */
public class Apirl {
	
	// =======================================================================
	// 21. 合并 k 个排序链表，返回合并后的排序链表
	// =======================================================================
	// 作弊解法
	public ListNode mergeKLists(ListNode[] lists) {
		ListNode dummyHead = new ListNode(-1), p = dummyHead;
		PriorityQueue<Integer> pq = new PriorityQueue<>();
		for (ListNode list : lists) {
			ListNode c = list;
			while (c != null) {
				pq.offer(c.val);
				c = c.next;
			}
		}
		while (!pq.isEmpty()) {
			p.next = new ListNode(pq.poll());
			p = p.next;
		}
		return dummyHead.next;
	}
	// 正确解法
	public ListNode mergeKLists2(ListNode[] lists) {
		ListNode dummyHead = new ListNode(-1), cur = dummyHead;
		// 若ListNode提供getVal方法，那么可以方法引用ListNode::getVal
		// PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparing(ListNode::getVal));
		PriorityQueue<ListNode> pq = new PriorityQueue<>((x1, x2) -> x1.val - x2.val);
		for (ListNode listNode : lists) {
			if (listNode != null)
				pq.offer(listNode);
		}
		while (!pq.isEmpty()) {
			ListNode top = pq.poll();
			cur.next = top;
			cur = cur.next;
			// 再添加回优先队列
			if (top.next != null)
				pq.offer(top.next);
		}
		return dummyHead.next;
	}

	// =======================================================================
	// 
	// =======================================================================

}
