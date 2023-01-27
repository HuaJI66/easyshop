package com.pika;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/23 19:24
 */
public class VarTest {
    @Test
    public void test1(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        map.put(1, list);
        System.out.println("map = " + map);
        list.add(4);
        System.out.println("map = " + map);
    }
}
