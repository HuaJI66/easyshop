package com.unionpay;

import com.unionpay.acp.sdk.CertUtil;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/3/1 10:26
 */
public class CommonTest {
    @Test
    public void test1(){
        CertUtil.init();
    }
    @Test
    public void test2() throws IOException {
        Path path = Paths.get("src/main/resources/META-INF/cert/acp_test_sign.pfx");
        InputStream inputStream = Files.newInputStream(path);
        System.out.println("inputStream.available() = " + inputStream.available());
    }
}
