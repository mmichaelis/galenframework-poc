package com.github.mmichaelis.galen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

/**
 * @see <a href="http://selenide.org/">Selenide: concise UI tests in Java</a>
 * @see <a href="https://github.com/codeborne/selenide/wiki/How-Selenide-creates-WebDriver">How Selenide creates WebDriver · codeborne/selenide Wiki</a>
 * @see com.codeborne.selenide.Configuration
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Test
@ExtendWith(GalenExtension.class)
public @interface GalenTest {
    /**
     * If {@code true} a browser restart will be enforced afterwards.
     *
     * @return if this test will dirty the browser (e. g. with cookies)
     */
    boolean dirtiesBrowser() default false;

    String specification() default "";
}
