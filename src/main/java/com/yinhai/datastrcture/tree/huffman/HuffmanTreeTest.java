package com.yinhai.datastrcture.tree.huffman;

import org.junit.Test;

/**
 * @Author: yangzl
 * @Date: 2019/11/17 16:30
 * @Desc: ..
 **/
public class HuffmanTreeTest {
	
	
	@Test
	public void testCreateHuffmanTree() {
		char[] arr = "i like like".toCharArray();
		HuffmanTree.createHuffman(arr);
	}

}
