package com.pika;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.json.JSONUtil;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/11/23 13:05
 */
public class Test {
    public void test() {
        MailAccount account = new MailAccount();
        account.setHost("smtp.qq.com");
        account.setPort(465);
        account.setAuth(true);
        account.setFrom("pikachu_plus@foxmail.com");
        account.setUser("pikachu_plus@foxmail.com");
        account.setPass("roeinybzevhjibgj");
        account.setSslEnable(Boolean.TRUE);
        account.setDebug(true);

        MailUtil.send(account, "pikachu_plus@foxmail.com", "测试", "测试邮件", false);
    }

    @org.junit.jupiter.api.Test
    public void test2() {
        MailUtil.send("pikachu_plus@foxmail.com", "OutLook", "您的验证码为: <h3>666</h3>", true);
    }

    @org.junit.jupiter.api.Test
    public void test3() {
        MailAccount account = new MailAccount();
        account.setHost("smtp.office365.com");
        account.setPort(587);
        account.setAuth(true);
        account.setUser("Alan Stim<GenshinTimeStamp@outlook.com>");
        account.setPass("1187443340@hotmail.com");
        account.setFrom("GenshinTimeStamp@outlook.com");
        account.setDebug(true);
        account.setStarttlsEnable(true);
        account.setSslEnable(Boolean.FALSE);
        account.setSocketFactoryClass("com.sun.mail.util.MailSSLSocketFactory");
        MailUtil.send(account, "pikachu_plus@foxmail.com", "com.pika.Test", "com.pika.Test", false);
    }

    @org.junit.jupiter.api.Test
    public void test4() {
        String json ="{skuId=1, spuId=1, skuName=Apple iPhone 14 Pro 暗紫色 8+128, skuDesc=null, catalogId=225, brandId=5, skuDefaultImg=https://gstore-piks.oss-cn-hangzhou.aliyuncs.com/2022/12/04/a90228bf-ba23-4dbb-8b20-b750f86d04d5_Snipaste_2022-12-04_19-21-57.png, skuTitle=Apple iPhone 14 Pro 暗紫色 8+128, skuSubtitle=支持移动联通电信5G 双卡双待手机, price=9429.0, saleCount=0}";
        SkuInfoVo skuInfoVo = JSONUtil.toBean(json, SkuInfoVo.class);
        System.out.println("skuInfoVo = " + skuInfoVo);
    }
    @org.junit.jupiter.api.Test
    public void test5(){
    }
}
