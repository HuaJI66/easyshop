package com.pika.controller;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/11/23 15:56
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class SendEmailController {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String name;

    // 上传文件存储目录
    @RequestMapping("/email")
    public void email(@RequestParam("email") String email, @RequestParam("title") String title, @RequestParam("content") String content) {
        // 构建一个邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置邮件主题
        message.setSubject(title);
        // 设置邮件发送者，这个跟application.yml中设置的要一致
        message.setFrom(name);
        // 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似
        // message.setTo("10*****16@qq.com","12****32*qq.com");
        message.setTo(email);
        // 设置邮件抄送人，可以有多个抄送人
        //message.setCc("12****32*qq.com");
        // 设置隐秘抄送人，可以有多个
        //message.setBcc("7******9@qq.com");
        // 设置邮件发送日期
        message.setSentDate(new Date());
        // 设置邮件的正文
        message.setText(content);
        // 发送邮件
        javaMailSender.send(message);
    }

    @GetMapping("send")
    public String send() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(name);
        message.setTo("pikachu_plus@foxmail.com");
        message.setSentDate(new Date());
        message.setText("Test");
        message.setSubject("Test");
        javaMailSender.send(message);
        return "success";
    }
}