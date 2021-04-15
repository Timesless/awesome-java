package com.yangzl.datastructure;

public interface Map<K, V> {

	/**
	 * 获取 size
	 *
	 * @return int map.size
	 */
	int size();

	/**
	 * map 是否为空
	 *
	 * @return bool
	 */
	boolean isEmpty();

	/**
	 * map 是否包含 k
	 *
	 * @param k k
	 * @return bool
	 */
	boolean contains(K k);

	/**
	 * 向 map 中添加元素
	 *
	 * @param k k
	 * @param v v
	 */
	void add(K k, V v);

	/**
	 * 删除 k，v 键值对，并返回 v
	 *
	 * @param k k
	 * @return v
	 */
	V remove(K k);

	/**
	 * 以 newV 替换 k 对应的 v
	 *
	 * @param k k
	 * @param newV new
	 */
	void set(K k, V newV);

	/**
	 * 获取 k 对应的 v
	 *
	 * @param k k
	 * @return v
	 */
	V get(K k);
}
