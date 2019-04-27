package com.bjp.helloworld.lock;

public class ExclusiveLockTester extends Thread {
    public static int count = 0;
    public ExclusiveLock exclusiveLock;

    public ExclusiveLockTester(ExclusiveLock exclusiveLock) {
        this.exclusiveLock = exclusiveLock;
    }

    @Override
    public void run() {
        try {
//            while (!this.sharedLock.tryLock()) {
//                Thread.sleep(10);
//            }
            this.exclusiveLock.lock();
            System.out.println(this.getName() + " get lock");
            System.out.println(++ExclusiveLockTester.count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.exclusiveLock.unlock();
        }
    }

    public static void main(String[] args) {
        ExclusiveLock exclusiveLock = new ExclusiveLock();
        for (int i = 0; i < 10; i++) {
            Thread t = new ExclusiveLockTester(exclusiveLock);
            t.setName("thread-" + i);
            t.start();
        }
    }
}