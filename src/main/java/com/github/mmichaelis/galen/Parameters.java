package com.github.mmichaelis.galen;

import com.galenframework.reports.GalenTestInfo;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.codeborne.selenide.WebDriverRunner.getAndCheckWebDriver;
import static com.github.mmichaelis.galen.Stores.GALEN_TEST_INFO;
import static com.github.mmichaelis.galen.Stores.getMethodLevelStore;

final class Parameters {
    private static final List<Class<?>> SUPPORTED_PARAMETER_TYPES = Collections.unmodifiableList(Arrays.asList(
            WebDriver.class,
            GalenTestInfo.class,
            Specification.class
    ));

    private Parameters() {
    }

    static boolean supportsParameter(ParameterContext parameterContext) {
        return SUPPORTED_PARAMETER_TYPES.stream().anyMatch(c -> isParameterOfType(parameterContext, c));
    }

    static boolean isParameterOfType(ParameterContext parameterContext, Class<?> supportedClass) {
        Class<?> parameterType = parameterContext.getParameter().getType();
        return parameterType.equals(supportedClass);
    }

    static Object resolveSpecificationParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Optional<GalenTest> annotation = Annotations.getAnnotation(extensionContext);
        String specResourceRaw = annotation.map(GalenTest::specification).orElse("");
        if (specResourceRaw.isEmpty()) {
            throw new ParameterResolutionException("Unable to resolve parameter " + parameterContext.getParameter() + " as the @GalenTest annotation does not specify a specification path.");
        }
        Class<?> testClass = extensionContext.getRequiredTestClass();
        return new SpecificationImpl(testClass.getResource(specResourceRaw).getPath(), getAndCheckWebDriver(), getMethodLevelStore(extensionContext).get(GALEN_TEST_INFO, GalenTestInfo.class));
    }

    static Object resolveWebDriverParameter() {
        try {
            return getAndCheckWebDriver();
        } catch (WebDriverException e) {
            throw new ParameterResolutionException("Failed to resolve WebDriver parameter as WebDriver instance could not be instantiated. Please check installed browsers or your system properties 'selenide.browser' and/or 'remote'.", e);
        }
    }

}
