package com.pika;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/7 20:54
 */
public class CompletableTest {

    private static final ExecutorService executor = Executors.newFixedThreadPool(200);

    public static void main(String[] args) {
        System.out.println("main start...");
//        CompletableFuture.runAsync(() -> {
//            System.out.println("执行异步任务");
//        }, executor);


        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return String.valueOf(10 / 0);
        }, executor);
        future.whenCompleteAsync((res,e) -> {
            System.out.println("结果res = " + res);
            System.out.println("异常e = " + e);
        }, executor).exceptionally(throwable->{
            System.out.println("throwable = " + throwable);
            return "出现异常返回默认值";
        });
        System.out.println("main end ....");
    }
}
class Test1{
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main start");
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return 1;
        }, executor).handleAsync((res, e) -> {
            if (res != null) {
                return ++res;
            }
            if (e != null) {
                return 0;
            }
            return 0;
        }, executor);
        System.out.println("main end res=:"+future.get());
    }
}
