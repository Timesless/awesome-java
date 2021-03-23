package com.yangzl;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangzl
 * @date 2021/3/12
 * @desc
 */
public class AppTest {

	// map.size() = 100
	@Test
	public void testMap() {
		Map<Short, String> map = new HashMap<>(128);
		for (short i = 0; i < 100; i++) {
			map.put(i, String.valueOf(i));
			map.remove(i-1);
		}
		System.out.println(map.size());
	}


}
