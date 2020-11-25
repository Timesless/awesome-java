package com.yangzl.contest;

import java.util.*;

/**
 * @author yangzl
 * @date 2020/11/15 10:31
 * @desc leetcode 235 京东联名周赛
 */

public class OrderedStream {
	
	
	private final TreeMap<Integer, String> map = new TreeMap<>();
    int ptr = 1;
	
	public OrderedStream(int n) {}
	
	/**
	 * 2020/11/15
	 * 
	 * @param id id
	 * @param  value value
	 * @return List
	 */
	public List<String> insert(int id, String value) {
		
		//	a		c	d	e
		//	1	2	3	4	5
		if (ptr == id) {
			ptr += 1;
			while (map.containsKey(ptr)) {
				++ ptr;
			}
		}
		map.put(id, value);
		if (id <= ptr) {
			List<String> rs = new ArrayList<>(ptr);
			for (int i = id; i < ptr; ++i) {
				rs.add(map.get(i));
			}
			return rs;
		} else {
			return new ArrayList<>(0);
		}
	}

	public static void main(String[] args) {
		OrderedStream os = new OrderedStream(9);
		System.out.println(os.insert(9, "nghbm"));
		System.out.println(os.insert(7, "hgeob"));
		System.out.println(os.insert(6, "mwlrz"));
		System.out.println(os.insert(4, "oalee"));
		System.out.println(os.insert(2, "bouhq"));
		System.out.println(os.insert(1, "mnknb"));
		System.out.println(os.insert(5, "qkxbj"));
		System.out.println(os.insert(8, "iydkk"));
		System.out.println(os.insert(3, "oqdnf"));
	}
}
