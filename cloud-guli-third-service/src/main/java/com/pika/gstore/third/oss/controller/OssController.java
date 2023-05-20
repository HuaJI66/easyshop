package com.pika.gstore.third.oss.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.pika.gstore.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/11/27 12:02
 */
@RestController
@RequestMapping("oss")
@Slf4j
public class OssController {
    @Resource
    private OSSClient ossClient;

    @Value("${alibaba.cloud.access-key}")
    private String accessId;
    @Value("${alibaba.cloud.oss.endpoint}")
    private String endpoint;
    @Value("${alibaba.cloud.oss.bucket}")
    String bucket;

    @RequestMapping("policy")
    public R oss() {
        // 填写Host地址，格式为https://bucketname.endpoint。
        String host = "https://" + bucket + "." + endpoint;
        // 设置上传回调URL，即回调服务器地址，用于处理应用服务器与OSS之间的通信。OSS会在文件上传完成后，把文件上传信息通过此回调URL发送给应用服务器。
//        String callbackUrl = "https://192.168.0.0:8888";
        // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
        String dir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        Map<String, String> respMap = new LinkedHashMap<>();
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);


            respMap.put("accessId", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));

        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            log.error(e.getMessage());
        }
        return R.ok().put("data", respMap);
    }
}
