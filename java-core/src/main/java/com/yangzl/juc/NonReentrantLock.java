package com.yangzl.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Author yangzl
 * @Date: 2020/6/7 11:41
 * @Desc: Java并发编程之美：基于AQS实现非重入锁
 *
 *  AQS是一个FIFO双向队列，通过head，tail记录队首队尾元素，队列元素类型Node
 *  Node中thread存放进入AQS队列的线程
 *  Node内部SHARED标记线程获取共享资源时被阻塞挂起放到AQS
 *  EXCLUSIVE标记线程获取独占资源时被挂起放入AQS
 *
 *  AQS维护一个单一状态信息state，通过getState，setState，compareAndSetState修改其值
 *      线程同步的关键是对状态值state操作
 *      根据state是否属于同一线程，操作state分为独占和共享
 *          独占： void acquire(int arg)
 *                void acquireInterruptibly(int arg)
 *                boolean release(int arg)
 *
 *          共享： void acquireShared(int arg)
 *                void acqureSharedInterruptibly(int arg)
 *                boolean releaseShared(int arg)
 *
 *  AQS内部类ConditionObject，每一个条件变量对应一个条件队列，用来存放嗲用条件变量await()后被阻塞的线程
 */
public class NonReentrantLock implements Lock {

    // 创建嵌套类对象
    private final Sync sync = new Sync();

    // 对外提供的接口
    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) {
        boolean flag = false;
        try {
            flag = sync.tryAcquireNanos(1, unit.toNanos(timeout));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }


    /**
     * 独占锁，需要重写：
     * isHeldExclusively()
     * tryAcquire(int arg)
     * tryRelease(int arg)
     *
     * 共享锁，需要重写：
     * tryAcquire(int arg)
     * tryRelease(int arg)
     */
    private static class Sync extends AbstractQueuedSynchronizer {

        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        /**
         * 2020/6/7 定义state含义，并设置state
         * @param acquires
         * @return
         */
        @Override
        protected boolean tryAcquire(int acquires) {
            assert acquires == 1;
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        /**
         * 根据state含义操作state
         */
        @Override
        protected boolean tryRelease(int releases) {
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        // 提供条件变量
        Condition newCondition() {
            return new ConditionObject();
        }
    }
}
