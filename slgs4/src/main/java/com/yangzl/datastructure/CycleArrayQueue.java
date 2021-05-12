package com.yangzl.datastructure;

import org.omg.CORBA.Object;

/**
 * @author yangzl
 * @date 2019/10/21 22:05
 *
 *  数组实现循环队列
 *  	入队：rear = ++rear % ele.length，出队：front = ++front % ele.length;
 * 		队满条件：留一个位置作为标识，当(rear + 1) % size == front队满
 * 		假设 size = 6， 当5个元素入队，那么 rear = 5 + 1 % size == front，队满
 * 		队空条件： rear == front
 * 		有效数据个数 ： (rear + size - front) % szie | Math.abs(rear - front) % size;
 */
public class CycleArrayQueue<E> {
	/** size当前队列元素的个数 */
	private int size, front, rear;
	private final E[] ele;
	/** 构造器 */
	public CycleArrayQueue(int len) { ele = (E[]) new Object[len + 1]; }
	
	/** 是否满队列 */
	public boolean isFull() { return (rear + 1 % ele.length) == front; }
	
	/** 规定以front == rear为空队列 */
	public boolean isEmpty() { return rear == front; }
	
	/** 有效数据size，修改为成员变量记录 */
	public int size() {
		return Math.abs(rear - front) % ele.length;
		// return rear + ele.length - front % ele.length;
	}

	/**
	 * 查看队头元素
	 *
	 * @return E front
	 */
	public E peek() {
		if (isEmpty()) { throw new IllegalArgumentException("空队列"); }
		return ele[front];
	}

	/**
	 * 出队
	 *
	 * @return E
	 */
	public E dequeue() {
		if (isEmpty()) { throw new IllegalArgumentException("空队列"); }
		E rs = ele[front];
		// 由于存储引用，所以显示赋值为null
		ele[front] = null;
		front = (front++) % ele.length;
		--size;
		return rs;
	}

	/**
	 * 入队
	 *
	 * @param e e
	 */
	public void enqueue(E e) {
		if (isFull()) { throw new IllegalArgumentException("满队列"); }
		ele[rear] = e;
		rear = (rear++) % ele.length;
		++size;
	}
	
	/**
	 * 循环数组，从front遍历size个元素
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		// 巧妙循环打印到len - 1个元素
		int x = front, len = front + this.size, count = len - 1;
		for (; x < count; ++x)
			sb.append(ele[x % ele.length]).append(", ");
		sb.append(ele[count]).append("]");
		return sb.toString();
	}

	/**
	 * main测试
	 *
	 * @param args args
	 */
	public static void main(String[] args) {
		CycleArrayQueue<Integer> queue = new CycleArrayQueue<>(5);
		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);
		queue.enqueue(4);
		System.out.println(queue.toString());
		queue.dequeue();
		System.out.println(queue.dequeue());
		System.out.println(queue.toString());
		System.out.println(queue.peek());
	}
}
