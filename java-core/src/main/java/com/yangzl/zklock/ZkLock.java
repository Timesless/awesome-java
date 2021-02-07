package com.yangzl.zklock;

/**
 * @author yangzl
 */
public interface ZkLock {

	/**
	 * 2020/5/31 加锁
	 */
	void zkLock();
	
	/**
	 * 2020/5/31 释放锁
	 */
	void zkUnLock();
}
