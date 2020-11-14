package com.yangzl;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: yangzl
 * @Date: 2020/5/16 13:11
 * @Desc: ..
 */
public class LocalVariablesTest {
	
	private static final int i = 2;
	private static final int j = 3;
	private static final Object obj = new Object();

	public static void main(String[] args) {

		List<byte[]> list = new ArrayList<>(20);
		while (true) {
			byte[] bytes = new byte[1 * 1024 * 1024];
			list.add(bytes);
			try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
		}
	}
	
	@Test
	public void testStringTableSize() {
		try { TimeUnit.SECONDS.sleep(Integer.MAX_VALUE); } catch (InterruptedException e) { e.printStackTrace(); }
	}
	

}
