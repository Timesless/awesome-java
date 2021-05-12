package com.yangzl.interview2;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yangzl
 * @date 2021/3/21
 *
 * java.lang.StackOverflowError
 *	递归
 *
 * java.lang.OutOfMemoryError: Java heap space
 *  new 对象
 *
 * java.lang.OutOfMemoryError: GC overhead limit exceeded
 * 花费 98% time 回收不到 2% 内存，产生此错误
 *
 * java.lang.OutOfMemoryError: Direct buffer memory
 * ByteBuffer.allocateDirect()`
 *
 * java.lang.OutOfMemoryError: unable to create new native thread
 *
 *
 * java.lang.OutOfMemory: Metaspace
 */
public class OOM {

	/**
	 * -Xms4m -Xmx4m -Xmn2m
	 */
	@Test
	public void testGCOverheadLimit() {
		List<String> list = new ArrayList<>();
		int i = 0;
		try {
			while (true) {
				list.add(String.valueOf(i++).intern());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * -Xms4m -Xmx4m -Xmn2m
	 *
	 * 默认情况下 -XX:MaxDirectMemorySize = -Xmx
	 * -XX:MaxDirectMemorySize=6m
	 */
	@Test
	public void testDirectOOM() {
		ByteBuffer.allocateDirect(5 * 1024 * 1024);
	}

}
