package com.pika;

import java.util.HashMap;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/3 14:33
 */
public class ExtendsTest {
    static {
        System.out.println("外部类静态代码块");
    }
}

class Father {
    private static HashMap<String, Object> map=new HashMap<>();

    static {
        map.put("1", 1);
        System.out.println("Father父类静态代码块");
    }

    public Father() {
        System.out.println("Father 构造方法");
    }

    {
        map.put("2", 2);
        System.out.println("Father父类非静态代码块");
    }

    public static void main(String[] args) {
        System.out.println("Father Main");
//        new Son();
        new Father();
        System.out.println("map = " + map);
    }
}
class Son extends Father{
    public static void main(String[] args) {
        System.out.println("Son Main");
//        new Son();
        new Father();
    }

    public Son() {
        System.out.println("Son 构造方法");
    }

    static {
        System.out.println("Son子类静态代码块");
    }
    {
        System.out.println("Son子类非静态代码块");
    }
}
