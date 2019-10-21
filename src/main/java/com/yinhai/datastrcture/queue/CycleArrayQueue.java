package com.yinhai.datastrcture.queue;

/**
 * @Author: yangzl
 * @Date: 2019/10/21 22:05
 * @Desc:
 * 数组实现循环队列
 * 	队满条件：留一个位置作为标识，当(rear + 1) % size == front队满
 * 		假设 size = 6， 当5个元素入队，那么 rear = 5 + 1 % size == front，队满
 * 	队空条件： rear == front
 * 	front = rear = 0
 * 	有效数据个数 ： (rear + size - front) % szie;
 **/
public class CycleArrayQueue {
	
	private int size, front, rear;
	private int[] ele;
	// 构造器
	public CycleArrayQueue(int _size) {
		this.size = _size + 1;
		front = rear = 0;
		ele = new int[size];
	}
	
	// 是否满队列
	public boolean isFull() {
		return (rear + 1 % size) == front;
	}
	
	// 是否空队列
	public boolean isEmpty() {
		return rear == front;
	}
	
	// size
	public int size() {
		return Math.abs(rear - front) % size;
		// return rear + size - front % size;
	}
	
	// 查看队头元素
	public int peek() {
		if(isEmpty())
			throw new IllegalArgumentException("空队列");
		return ele[front];
	}
	
	// 出队
	public int dequeue() {
		if(isEmpty())
			throw new IllegalArgumentException("空队列");
		int rs = ele[front];
		front = ++front % size;
		return rs;
	}
	
	// 入队
	public int enqueue(int n) {
		if (isFull())
			throw new IllegalArgumentException("满队列");
		ele[rear] = n;
		rear = ++rear % size;
		return n;
	}
	
	// 循环数组，从front遍历size个元素
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		int x = front, len = front + this.size(), count = len - 1;
		for (; x < count; ++x)
			sb.append(ele[x % size]).append(", ");
		sb.append(ele[count]).append("]");
		return sb.toString();
	}

	// main测试
	public static void main(String[] args) {
		CycleArrayQueue queue = new CycleArrayQueue(5);
		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);
		queue.enqueue(4);
		System.out.println(queue.toString());
		queue.dequeue();
		queue.dequeue();
		System.out.println(queue.toString());
		System.out.println(queue.peek());
	}
	
	
}
