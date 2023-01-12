package com.pika.gstore.third;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.core.http.HttpClient;
import com.aliyun.core.http.HttpMethod;
import com.aliyun.core.http.ProxyOptions;
import com.aliyun.httpcomponent.httpclient.ApacheAsyncHttpClientBuilder;
import com.aliyun.sdk.service.dysmsapi20170525.models.*;
import com.aliyun.sdk.service.dysmsapi20170525.*;
import com.google.gson.Gson;
import darabonba.core.RequestConfiguration;
import darabonba.core.client.ClientOverrideConfiguration;
import darabonba.core.utils.CommonUtil;
import darabonba.core.TeaPair;

//import javax.net.ssl.KeyManager;
//import javax.net.ssl.X509TrustManager;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    @Value("${alibaba.cloud.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${alibaba.cloud.sms.accessKeySecret}")
    private String accessKeySecret;

    @Test
    public void test1() {
        ossClient.getObject(new GetObjectRequest("gstore-piks", "spyfamily/yueer.jpg"), new File("D://tmp/yueer.jpg"));
    }

    @Test
    public void test2() {
        log.info("ossClient.getEndpoint() = " + ossClient.getEndpoint());
    }

    @Test
    public void test3() throws ExecutionException, InterruptedException {

            // Configure Credentials authentication information, including ak, secret, token
            StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                    .accessKeyId(accessKeyId)
                    .accessKeySecret(accessKeySecret)
                    //.securityToken("<your-token>") // use STS token
                    .build());

            // Configure the Client
            AsyncClient client = AsyncClient.builder()
                    .region("cn-hangzhou") // Region ID
                    //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                    .credentialsProvider(provider)
                    //.serviceConfiguration(Configuration.create()) // Service-level configuration
                    // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                    .overrideConfiguration(
                            ClientOverrideConfiguration.create()
                                    .setEndpointOverride("dysmsapi.aliyuncs.com")
                            //.setConnectTimeout(Duration.ofSeconds(30))
                    )
                    .build();

            // Parameter settings for API request
            SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                    .signName("阿里云短信测试")
                    .templateCode("SMS_154950909")
                    .phoneNumbers("18319098362")
                    .templateParam("{\"code\":\"8888\"}")
                    // Request-level configuration rewrite, can set Http request parameters, etc.
                    // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                    .build();

            // Asynchronously get the return value of the API request
            CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
            // Synchronously get the return value of the API request
            SendSmsResponse resp = response.get();
            System.out.println(new Gson().toJson(resp));

            // Finally, close the client
            client.close();
    }
}
