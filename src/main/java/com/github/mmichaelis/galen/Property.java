package com.github.mmichaelis.galen;

import org.junit.jupiter.api.Tags;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Repeatable(Properties.class)
public @interface Property {
    String key();

    String value();
}
