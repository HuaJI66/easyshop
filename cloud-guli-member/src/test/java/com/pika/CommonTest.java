package com.pika;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.crypto.digest.MD5;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/10 18:17
 */
public class CommonTest {
    @Test
    public void test1(){
        String md5 = SecureUtil.md5("12345");
        System.out.println("md5 = " + md5);
        md5 = SecureUtil.md5("12345");
        System.out.println("md5 = " + md5);
        byte[] decode = SecureUtil.decode("827ccb0eea8a706c4c34a16891f84e7b");
        System.out.println("new String(decode) = " + new String(decode));
    }
    @Test
    public void test2(){
        //huTool
        // $2a$10$HGWZrtojpX4POBWrmQFL9OjJBxSVBDEqsy9Ue4DSc2hYaW8YvhJ1q
        // $2a$10$yQtLP1KTKGZ/.PrFyMqMy.jdGbjIYbdGp/4pamejZZNwothg35ir2
        // 加密
        String encode = BCrypt.hashpw("12345");
        //plaintext – 需要验证的明文密码 hashed – 密文
        boolean checkpw = BCrypt.checkpw("12345", encode);
        System.out.println("encode = " + encode);
        System.out.println("checkpw = " + checkpw);
    }
    @Test
    public void test3(){
        //org.springframework.security.crypto.bcrypt
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("12345");
        boolean matches = encoder.matches("12345", encode);
        System.out.println("encode = " + encode);
        System.out.println("matches = " + matches);
    }
}
