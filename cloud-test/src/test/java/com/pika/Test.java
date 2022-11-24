package com.pika;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;

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

    public void test4() {

    }
}
