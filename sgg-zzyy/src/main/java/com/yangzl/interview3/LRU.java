package com.yangzl.interview3;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yangzl
 * @date 2021/3/23
 *
 * 增删 O1，查找 O1
 *
 * 		只能打组合，Map + LinkedList === LinkedHashMap
 */
public class LRU {

	static class MyLRU extends LinkedHashMap<String, Object> {
		private int capacity;

		public MyLRU(int capacity) {
			super(capacity << 1, 0.75f, true);
			this.capacity = capacity;
		}
		@Override
		protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
			return super.size() > capacity;
		}
	}

}
