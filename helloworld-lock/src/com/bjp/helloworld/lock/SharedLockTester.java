package com.bjp.helloworld.lock;

public class SharedLockTester extends Thread {
    public SharedLock sharedLock;

    public SharedLockTester(SharedLock sharedLock) {
        this.sharedLock = sharedLock;
    }

    @Override
    public void run() {
        try {
//            while (!this.sharedLock.tryLock()) {
//                Thread.sleep(10);
//            }
            this.sharedLock.lock();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SharedLock sharedLock = new SharedLock(8);
        for (int i = 0; i < 100; i++) {
            Thread t = new SharedLockTester(sharedLock);
            t.setName("thread-" + i);
            t.start();
        }
        sharedLock.release();
    }
}