package com.github.mmichaelis.galen;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Repeatable(JavaScriptVariables.class)
public @interface JavaScriptVariable {
    String name();

    String value();
}
