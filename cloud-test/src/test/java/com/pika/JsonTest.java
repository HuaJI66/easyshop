package com.pika;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/12/30 22:09
 */
public class JsonTest {
    public static class IntMap extends HashMap<String, Integer> {
    }

    @Test
    public void test1() {
        IntMap intMap = new IntMap();
        System.out.println("getSuperclass:" + intMap.getClass().getSuperclass());
        System.out.println("getGenericSuperclass:" + intMap.getClass().getGenericSuperclass());
        Type type = intMap.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            for (Type t : p.getActualTypeArguments()) {
                System.out.println(t);
            }
        }
    }

    /*
        getSuperclass:class java.util.HashMap
        getGenericSuperclass:java.util.HashMap<java.lang.String, java.lang.Integer>
        class java.lang.String
        class java.lang.Integer
    */
    @Test
    public void test2() {
        Map<String, Integer> intMap = new HashMap<>();
        System.out.println("\ngetSuperclass:" + intMap.getClass().getSuperclass());
        System.out.println("getGenericSuperclass:" + intMap.getClass().getGenericSuperclass());
        Type type = intMap.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            for (Type t : p.getActualTypeArguments()) {
                System.out.println(t);
            }
        }
    }

    /*
        getSuperclass:class java.util.AbstractMap
        getGenericSuperclass:java.util.AbstractMap<K, V>
        K
        V
    */
    @Test
    public void test3() {
        Map<String, Integer> intMap = new HashMap<String, Integer>() {
        };
        System.out.println("\ngetSuperclass:" + intMap.getClass().getSuperclass());
        System.out.println("getGenericSuperclass:" + intMap.getClass().getGenericSuperclass());
        Type type = intMap.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            for (Type t : p.getActualTypeArguments()) {
                System.out.println(t);
            }
        }
    }

    /*
    getSuperclass:class java.util.HashMap
    getGenericSuperclass:java.util.HashMap<java.lang.String, java.lang.Integer>
    class java.lang.String
    class java.lang.Integer　
    */
    @Test
    public void test4() {
        R r = new R();
        r.setData(Arrays.asList(1, 2, 3, 4, 5));
        List<String> data = r.getData(new TypeReference<List<String>>() {
        });
        System.out.println(data);
    }


//    public Map<K, V> test6(K key, V value) {
//        HashMap<K, V> kvHashMap = new HashMap<K, V>();
//        kvHashMap.put(key, value);
//        return kvHashMap;
//    }

    public <K, V> Map<K, V> test5(K key, V value) {
        HashMap<K, V> kvHashMap = new HashMap<>();
        kvHashMap.put(key, value);
        return kvHashMap;
    }

    private static class R1 extends HashMap<String, Object> {
        private static final long serialVersionUID = 1L;

        public <T> T getData(TypeReference<T> typeReference) {
            Object data = this.get("data");
            String jsonStr = JSONUtil.toJsonStr(data);
            T bean = JSONUtil.toBean(jsonStr, typeReference, false);
            return bean;
        }

        public void setData(Object data) {
            this.put("data", data);
        }
    }
    private static class R2<T> extends HashMap<String, Object> {
        private static final long serialVersionUID = 1L;

        public  T getData() {
            return (T) get("data");
        }

        public void setData(T data) {
            this.put("data", data);
        }
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class User {
        private Integer id;
        private String name;
    }
    @Test
    public void test7(){
        User user1 = new User(1, "user1");
        User user2 = new User(2, "user2");

        R1 r1 = new R1();
        r1.setData(Arrays.asList(user1, user2));
        System.out.println("r1获取数据....");
        r1.getData(new TypeReference<List<User>>() {}).forEach(System.out::println);

        R2<List<User>> r2 = new R2<>();
        r2.setData(Arrays.asList(user1, user2));
        System.out.println("r2获取数据....");
        r2.getData().forEach(System.out::println);
    }
}

