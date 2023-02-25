package com.pika.annotation;

import java.lang.annotation.*;

/**
 * @author pi'ka'chu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Login {
    String value() default "";
}
