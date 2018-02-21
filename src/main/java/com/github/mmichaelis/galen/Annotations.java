package com.github.mmichaelis.galen;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.util.AnnotationUtils;

import java.util.Optional;

final class Annotations {
    private Annotations() {
    }

    static Optional<GalenTest> getAnnotation(ExtensionContext extensionContext) {
        return AnnotationUtils.findAnnotation(extensionContext.getElement(), GalenTest.class);
    }
}
