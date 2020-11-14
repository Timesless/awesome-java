package com.yangzl.zklock;

public interface ZkLock {
	
	/**
	 * @Date: 2020/5/31
	 * @Desc: 加锁
	 */
	void zkLock();
	
	/**
	 * @Date: 2020/5/31
	 * @Desc: 释放锁
	 */
	void zkUnLock();
}
