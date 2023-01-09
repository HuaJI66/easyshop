package com.pika;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/6 23:00
 */
public class ThreadTest {

    private static final ExecutorService pool = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
//        FutureTask<Long> task=new FutureTask<>(new Callable01());
//        new Thread(task).start();
        for (int i = 0; i < 10; i++) {
            pool.submit(new Callable01());
        }
    }

    static class Callable01 implements Callable<Long> {

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        public Long call() throws Exception {
            System.out.println("当前线程"+Thread.currentThread().getName());
            System.out.println("当前线程id: "+Thread.currentThread().getId());
            return Thread.currentThread().getId();
        }
    }
}
