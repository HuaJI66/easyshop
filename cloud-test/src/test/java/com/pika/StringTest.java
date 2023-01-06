package com.pika;

import org.junit.Test;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/6 14:15
 */
public class StringTest {
    @Test
    public void test1(){
        String str = "hello world";
        str.replace("world", "");
        System.out.println("str = " + str);
    }
}
