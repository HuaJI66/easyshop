package com.pika.gstore.auth;

import cn.hutool.core.util.URLUtil;
import com.sun.javafx.binding.StringFormatter;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/11 20:35
 */
public class CommonTest {
    @Test
    public void test1(){
        String str = "https://gitee.com/oauth/token?grant_type=authorization_code&code={code}&client_id={client_id}&redirect_uri={redirect_uri}&client_secret={client_secret}";
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", 123);
        map.put("client_id", 456);
        System.out.println(URLUtil.buildQuery(map, StandardCharsets.UTF_8));
    }
    @Test
    public void test2(){
        String base = "https://gitee.com/oauth/token?grant_type=authorization_code&";
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", "");
        URLUtil.buildQuery(map, StandardCharsets.UTF_8);
    }
}
