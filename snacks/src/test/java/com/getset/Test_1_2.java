package com.getset;

import com.getset.chapter_1_2.MyAtomicInteger;
import org.junit.Test;

public class Test_1_2 {
    /**
     * 1. 响应式之道 - 2 响应式流 （附录A）
     *
     * 通过扩展 AtomicInteger 实现了自定义的 AtomicInteger， 可以记录“Compare and Set”的失败次数。
     */
    @Test
    public void testCustomizeAtomic() throws InterruptedException {
        final MyAtomicInteger myAtomicInteger = new MyAtomicInteger();
        Thread[] incs = new Thread[10];
        Thread[] decs = new Thread[10];
        for (int i = 0; i < incs.length; i++) {
            incs[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    myAtomicInteger.inc();
                }
            });
            incs[i].start();
            decs[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    myAtomicInteger.dec();
                }
            });
            decs[i].start();
        }

        for (int i = 0; i < 10; i++) {
            incs[i].join();
            decs[i].join();
        }

        System.out.println(myAtomicInteger.get() + " with " + myAtomicInteger.getFailureCount() + " failed tries.");
    }
}
