package com.github.mmichaelis.galen;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Repeatable(SectionFilter.class)
public @interface SectionTag {
    String value();
}
