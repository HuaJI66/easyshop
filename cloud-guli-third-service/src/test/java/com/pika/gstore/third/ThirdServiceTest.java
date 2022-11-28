package com.pika.gstore.third;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/11/27 11:33
 */
@SpringBootTest
@Slf4j
public class ThirdServiceTest {
    @Resource
    private OSSClient ossClient;

    @Test
    public void test1() {
        ossClient.getObject(new GetObjectRequest("gstore-piks", "spyfamily/yueer.jpg"), new File("D://tmp/yueer.jpg"));
    }

    @Test
    public void test2() {
        log.info("ossClient.getEndpoint() = " + ossClient.getEndpoint());
    }

    @Test
    public void test3(){

    }
}
