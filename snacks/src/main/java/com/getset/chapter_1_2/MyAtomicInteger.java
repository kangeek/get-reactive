package com.getset.chapter_1_2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 1. 响应式之道 - 2 响应式流 （附录A）
 *
 * 从两个方法 inc 和 dec 可以看出 Atomic* 的原子性的实现原理：
 * 这是一种乐观锁，每次修改值都会【先比较再赋值】，这个操作在CPU层面是原子的，从而保证了其原子性。
 * 如果比较发现值已经被其他线程变了，那么就返回 false，然后重新尝试。
 *
 * <p>高并发情况下，如果计算时间比较长，那么就容易陷入总是被别人更新的状态，而导致性能急剧下降，
 * 比如通过去掉inc和dec方法中的注释运行一下就会出现这种情况。
 */
public class MyAtomicInteger extends AtomicInteger {
    private AtomicLong failureCount = new AtomicLong(0);

    public long getFailureCount() {
        return failureCount.get();
    }

    public void inc() {
        Integer value;
        do {
            value = get();
            failureCount.getAndIncrement();
//            try {
//                TimeUnit.MILLISECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        } while (!compareAndSet(value, value + 1)); // compareAndSet方法是原子性的“先比较再赋值”的方法实现
    }

    public void dec() {
        Integer value;
        do {
            value = get();
            failureCount.getAndIncrement();
//            try {
//                TimeUnit.MILLISECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        } while (!compareAndSet(value, value - 1));
    }
}
