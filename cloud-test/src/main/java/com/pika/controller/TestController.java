package com.pika.controller;

import com.pika.pojo.Book;
import com.pika.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/14 21:10
 */
@RestController
public class TestController {
    @GetMapping("/sameName")
    public String test1(User user, Book book){
        return user.toString() + book.toString();
    }
}
