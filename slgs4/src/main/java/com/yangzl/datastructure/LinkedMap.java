package com.yangzl.datastructure;

/**
 * @Author yangzl
 * @Date 2020/2/11 21:11
 * @Desc .. 链表实现映射
 * 	当要找到前一个元素时使用dummyHead，当要找到当前元素时使用dummyHead.next
 */
public class LinkedMap<K, V> implements Map<K, V> {
	private final Node<K, V> dummyHead;
	private int size;
	public LinkedMap() { this.dummyHead = new Node<>(null, null); }
	
	static class Node<K, V> {
		K k;
		V v;
		Node<K, V> next;
		public Node(K k, V v) {
			this.k = k;
			this.v = v;
		}
		public Node(K k, V v, Node<K, V> next) {
			this.k = k;
			this.v = v;
			this.next = next;
		}
	}

	@Override
	public int size() { return size; }
	@Override
	public boolean isEmpty() { return size == 0; }

	@Override
	public boolean contains(K k) { return get(k) != null; }

	@Override
	public void add(K k, V v) {
		Node<K, V> targetNode = getNode(k);
		if (null == targetNode) {
			dummyHead.next = new Node<>(k, v, dummyHead.next);
			size++;
		} else 
			targetNode.v = v;
	}

	@Override
	public V remove(K k) {
		if (isEmpty()) throw new IllegalArgumentException("空");
		Node<K, V> p = dummyHead;
		while (p.next != null) {
			if (k.equals(p.next.k))
				break;
			p = p.next;
		}
		V res = p.v;
		p.next = p.next.next;
		size--;
		return res;
	}

	@Override
	public void set(K k, V newV) {
		Node<K, V> target = getNode(k);
		if (null != target)
			target.v = newV;
	}

	@Override
	public V get(K k) {
		Node<K, V> res = getNode(k);
		return res == null ? null : res.v;
	}
	
	// 获取k对应的节点
	private Node<K, V> getNode(K k) {
		Node<K, V> p = dummyHead.next;
		while (p != null)  {
			if (k.equals(p.k))
				return p;
			p = p.next;
		}
		return null;
	}
}
