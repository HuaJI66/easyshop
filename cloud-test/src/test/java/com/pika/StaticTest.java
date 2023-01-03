package com.pika;

import org.junit.Test;

import java.util.HashSet;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/3 14:21
 */
public class StaticTest {
    static {
        HashSet<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        System.out.println("set = " + set);
    }

    public StaticTest() {
        System.out.println("执行构造方法");
    }

    public static void main(String[] args) {
        new StaticTest();
        new StaticTest();
    }
}
