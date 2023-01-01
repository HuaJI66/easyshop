package com.pika;

import org.junit.Test;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/12/31 23:32
 */
public class StreamTest {
    @Test
    public void test1(){
        Map<Integer, Integer> collect = Arrays.asList(1, 2, 3, 4, 5).stream().filter(i -> i > 3).collect(Collectors.toMap(i -> i, j -> j));
        System.out.println("collect = " + collect);
    }
}
