package com.yangzl.zklock;

import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.CountDownLatch;

/**
 * @Author yangzl
 * @Date 2020/5/31 22:38
 * @Desc .. 模板设计模式
 */
public abstract class AbstractZkLock implements ZkLock {
	
	private static final String ZK_SERVER = "127.0.0.1:2181";
	/** 所有线程在同一个路径创建zk node */
	protected String nodePath = "/distributed";
	protected ZkClient zkClient  = new ZkClient(ZK_SERVER, 45 * 1000);
	
	protected CountDownLatch countDownLatch;

	@Override
	public void zkLock() {
		if (tryLock()) {
			System.out.printf("%s 占用锁成功\n", Thread.currentThread().getName());
		} else {
			waitZkLock();
			
			// 递归思想
			zkLock();
		}
	}

	@Override
	public void zkUnLock() {
		zkClient.close();
		System.out.printf("%s 释放锁\n", Thread.currentThread().getName());
	}

	/**
	 * 尝试获取锁
	 *
	 * @return bool
	 */
	protected abstract boolean tryLock();

	/**
	 * 等待获取锁
	 */
	protected abstract void waitZkLock();
}
