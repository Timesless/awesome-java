package com.yangzl.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * @date 2020/4/8
 * @desc 设计实现 LFU 缓存
 * 
 * 时间复杂度较高，缓存满之后remove都需要构造一个PriorityQueue，可以使用LinkedHashMap来做LRU（当frq相同时按LRU排）
 * TODO LinkedHashMap
 * 
 * 	2020年11月15日
 * 	如果想要达到O(1)时间复杂度，那么数据结构应该是 cache: Map<Node, Value> + frequency： Map<frq, LinkedList<Integer>>
 *  每一个频率都分配一个链表，在访问时置换到队头，当频率相同时删除链表中的队尾数据
 *  
 *  Node 节点至少需要 key和 frq。  那么 frequency 可以使用 LinkedHashMap来实现，使用访问顺序的构造器
 *  	即： new LinkedHashMap<>(true)
 */
public class LFUCache {

	private static class Node implements Comparable<Node> {
		int key, val, frq, time;
		public Node(int key, int val) {
			this.key = key;
			this.val = val;
			this.time = t++;
		}
		@Override
		public int compareTo(Node o) {
			return frq == o.frq ? time - o.time : frq - o.frq;
		}
	}

	Map<Integer, Node> map;
	int sz;
	// 当作全局的时间戳
	static int t = 0;

	public LFUCache(int capacity) {
		sz = capacity;
		map = new HashMap<>(capacity);
	}

	public int get(int key) {
		if (sz == 0 || !map.containsKey(key)) {
			return -1;
		}
		Node node = map.get(key);
		node.frq ++;
		node.time = t++;
		return node.val;
	}

	public void put(int key, int value) {
		if (sz == 0) return;
		if (map.containsKey(key)) {
			Node node = map.get(key);
			node.val = value;
			node.frq ++;
			node.time = t++;
		} else {
			if (map.size() == sz) {
				/* 移除最不经常使用的数据，平局按最近最少使用排
				 * 使用全局的pq不能实现，因为当修改节点的frq 和 time属性之后，PriorityQueue内部并不会重新排列元素大小
				 * 有序数据结构 SortedSet / SortedMap 修改节点compareTo计算的属性值之后，数据结构内部并不会计算排列
				 * 所以只能每次构造一个最小堆。
				 */
				PriorityQueue<Node> heap = new PriorityQueue<>(map.values());
				map.remove(heap.peek().key);
			}
			map.put(key, new Node(key, value));
		}
	}

	public static void main(String[] args) {
		LFUCache cache = new LFUCache(3);
		cache.put(2, 2);
		cache.put(1, 1);
		System.out.println(cache.get(2));
		System.out.println(cache.get(1));
		System.out.println(cache.get(2));

		cache.put(3, 3);
		cache.put(4, 4);
		System.out.println(cache.get(3));	// -1
		System.out.println(cache.get(2));	// 2
		System.out.println(cache.get(1));	// 1
		System.out.println(cache.get(4));	// 4

		LFUCache cache1 = new LFUCache(2);

	}
}
