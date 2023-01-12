package com.pika.gstore.member.exception;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/10 17:29
 */
public class UsernameExistException extends RuntimeException{
    public UsernameExistException() {
        super("用户名已存在");
    }
}
