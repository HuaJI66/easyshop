package com.pika;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/22 12:59
 */
public class ReduceTest {
    @Test
    public void test1() {
        BigDecimal decimal1 = new BigDecimal("12.5");
        BigDecimal decimal2 = new BigDecimal("13.5");
        BigDecimal decimal3 = new BigDecimal("14.5");
        BigDecimal decimal4 = new BigDecimal("15.5");
        List<BigDecimal> list = Arrays.asList(decimal1, decimal2, decimal3, decimal4);
        BigDecimal reduce = list.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("reduce = " + reduce);
    }
}
