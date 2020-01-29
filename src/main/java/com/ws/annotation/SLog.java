package com.ws.annotation;

import java.lang.annotation.*;

/**
 * @author WS-
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SLog {
    String operate();
}
