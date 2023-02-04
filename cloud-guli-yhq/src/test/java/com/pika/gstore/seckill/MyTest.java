package com.pika.gstore.seckill;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/1 23:48
 */
public class MyTest {
    @Test
    public void test1(){
        LocalDate now = LocalDate.now();
        LocalDateTime start = LocalDateTime.of(now, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(now.plusDays(2), LocalTime.MAX);
        System.out.println("start = " + start.format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")));
        System.out.println("end = " + end.format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")));
    }
}
