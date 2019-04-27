package com.bjp.helloworld.lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 共享锁
 */
public class SharedLock {
    private final Sync sync = new Sync();
    private AtomicInteger count;
    private int limit;

    public SharedLock(int limit) {
        this.limit = limit;
        this.count = new AtomicInteger(0);
    }

    public void lock() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public void release() {
        while (true) {
            if (count.get() > limit) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sync.releaseShared(1);
            }
        }
    }

    private class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected int tryAcquireShared(int arg) {
            int newcount = count.incrementAndGet();
            if (newcount > limit) {
                return -1;
            }
            System.out.println(Thread.currentThread().getName() + ":" + newcount);
            return 1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            count.set(0);
            System.out.println("----------------------------");
            return true;
        }
    }
}
