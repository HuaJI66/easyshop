package com.pika.gstore.seckill;

import com.alibaba.nacos.common.utils.DateFormatUtils;
import com.pika.gstore.seckill.service.SeckillConstant;
import com.pika.gstore.seckill.test.B;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//@SpringBootTest
public class SeckillApplicationTests extends B {

    @Test
    void contextLoads() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        Integer[] array = list.toArray(new Integer[4]);
        System.out.println("array = " + Arrays.toString(array));
    }

    @Test
    public void test1() {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("localDate = " + localDate);
        System.out.println("localDateTime = " + localDateTime);
        Date date = new Date(1675267680000L);
        System.out.println("new Date(1675267680000L) = " + date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date);
        System.out.println("format = " + format);
    }

    @Test
    public void test2() {
        Date date = new Date();
        String format = DateFormatUtils.format(date, SeckillConstant.SECKILL_DATEFORMAT);
        System.out.println("format = " + format);
    }

    @Test
    public void test3() {
        LocalDate date = LocalDate.of(2023, 2, 28);
        ArrayList<String> keys = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            keys.add(SeckillConstant.SESSION_CACHE_PREFIX + date.plusDays(i));
        }
        keys.forEach(System.out::println);
    }

    @Test
    public void test4() {
        long x = 1675267680000L;
        int y = (int) x;
        System.out.println("y = " + y);
    }
    @Test
    public void test5(){
        SeckillApplicationTests tests = new SeckillApplicationTests();
        System.out.println("tests.toString() = " + tests.toString());
        System.out.println("tests.hashCode() = " + tests.hashCode());
        SeckillApplicationTests tests1 = new SeckillApplicationTests();
        System.out.println("tests1.hashCode() = " + tests1.hashCode());
        System.out.println("tests.equals(tests1) = " + tests.equals(tests1));
    }
}
