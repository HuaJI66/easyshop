package com.pika.gstore.auth.exception;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/25 9:00
 */
public class LoginException extends RuntimeException{
    public LoginException(String message) {
        super(message);
    }
}
