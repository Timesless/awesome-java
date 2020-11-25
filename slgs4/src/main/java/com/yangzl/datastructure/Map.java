package com.yangzl.datastructure;

public interface Map<K, V> {
	int size();
	boolean isEmpty();
	boolean contains(K k);
	void add(K k, V v);
	V remove(K k);
	void set(K k, V newV);
	V get(K k);
}
