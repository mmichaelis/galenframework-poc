package com.github.mmichaelis.galen;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SectionFilter {
    SectionTag[] value();
    SectionTag[] excluded() default {};
}
