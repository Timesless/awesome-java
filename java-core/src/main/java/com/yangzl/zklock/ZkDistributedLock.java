package com.yangzl.zklock;

import org.I0Itec.zkclient.IZkDataListener;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: yangzl
 * @Date: 2020/5/31 22:48
 * @Desc: .. zk实现分布式锁
 */
public class ZkDistributedLock extends AbstractZkLock {

	/**
	 * @Date: 2020/5/31
	 * @Desc: 父类提供模板，子类提供实现
	 */
	@Override
	public boolean tryLock() {
		try {
			// 创建节点成功则返回true
			super.zkClient.createEphemeral(nodePath, "hhh");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void waitZkLock() {
		IZkDataListener listener = new IZkDataListener() {
			@Override
			public void handleDataChange(String s, Object o) throws Exception {
				
			}
			@Override
			public void handleDataDeleted(String s) throws Exception {
				if (countDownLatch != null) {
					countDownLatch.countDown();
				}
			}
		};
		zkClient.subscribeDataChanges(nodePath, listener);
		
		// 节点已经被其它线程创建，那么等待
		if (zkClient.exists(nodePath)) {
			countDownLatch = new CountDownLatch(1);
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		zkClient.unsubscribeAll();
	}
}
