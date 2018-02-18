package com.github.mmichaelis.galen;

import org.junit.jupiter.api.Tags;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SpecificationPath {
    String value();
}
