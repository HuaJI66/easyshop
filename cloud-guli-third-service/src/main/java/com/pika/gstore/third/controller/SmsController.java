package com.pika.gstore.third.controller;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponseBody;
import com.pika.gstore.common.exception.BaseException;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.common.constant.ThirdServiceConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/9 19:08
 */
@RestController
@RequestMapping("sms")
@Slf4j
public class SmsController {
    @Resource
    private AsyncClient asyncClient;
    @Resource
    private Client client;

    @GetMapping("sendAsync")
    public R sendAsync(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            return R.error(BaseException.MISS_ERROR.getCode(),
                    BaseException.MISS_ERROR.getMsg());
        }
        try {
            // Parameter settings for API request
            SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                    .signName("阿里云短信测试")
                    .templateCode("SMS_154950909")
                    .phoneNumbers(phone)
                    .templateParam("{\"code\":\"" + code + "\"}")
                    // Request-level configuration rewrite, can set Http request parameters, etc.
                    // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                    .build();
            // Asynchronously get the return value of the API request
            CompletableFuture<SendSmsResponse> response = asyncClient.sendSms(sendSmsRequest);
            // Synchronously get the return value of the API request
            SendSmsResponseBody body = response.get().getBody();
            boolean resultCode = "OK".equalsIgnoreCase(body.getCode());
            return resultCode ? R.ok() : R.ok(body.getMessage()).put("code", BaseException.OTHER_ERROR.getCode());
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage());
            return R.error();
        } finally {
            // Finally, close the client
            asyncClient.close();
        }
    }

    @GetMapping("send")
    public R send(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            return R.error(BaseException.MISS_ERROR.getCode(),
                    BaseException.MISS_ERROR.getMsg());
        }
        // 工程代码泄露可能会导致AccessKey泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName("阿里云短信测试")
                .setTemplateCode("SMS_154950909")
                .setPhoneNumbers(phone)
                .setTemplateParam("{\"code\":\"" + code + "\"}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            com.aliyun.dysmsapi20170525.models.SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, runtime);
            boolean resultCode = "OK".equalsIgnoreCase(response.body.getCode());
            return resultCode ? R.ok() : R.ok(response.body.getMessage()).put("code", BaseException.OTHER_ERROR.getCode());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return R.error(BaseException.OTHER_ERROR.getCode(), BaseException.OTHER_ERROR.getMsg());
    }
}
