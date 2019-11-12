package com.yinhai.datastrcture.linkedlist;

import java.util.LinkedList;

/**
 * @Author: yangzl
 * @Date: 2019/10/27 15:22
 * @Desc: ..	双向链表
 * 
 * 		可实现自我删除，找到要删除的节点 tmp
 * 			Node prev = tmp.prev;	// 因为有头节点，所以这里可直接 tmp.prev.next = tmp.next
 * 			Node next = tmp.next;
 * 			
 * 				tmp.prev.next = tmp.next;
 * 				if(null != tmp.next){
 *  * 			 tmp.next.prev = tmp.prev;	// 最后一个节点，这里会NPE
 *  * 			}	
 * 				
 * 		添加：index - 1
 * 			newNode.next = tmp.next;
 * 			tmp.next = newNode;		
 * 			tmp.next.prev = newNode;
 * 			tmp = newNode.prev;	
 * 			
 **/
public class DoubleLinkedList {

}
