package com.github.mmichaelis.galen;

import com.codeborne.selenide.Selenide;
import com.galenframework.reports.GalenTestInfo;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.platform.commons.util.AnnotationUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.util.LinkedList;
import java.util.Optional;

import static com.codeborne.selenide.WebDriverRunner.getAndCheckWebDriver;

public class GalenExtension implements BeforeAllCallback, AfterEachCallback, ParameterResolver {
    private static final String GALEN_TEST_INFOS = "galenTestInfos";

    private static Store getInstanceLevelStore(ExtensionContext context) {
        return context.getStore(getInstanceLevelNamespace(context));
    }

    private static Namespace getInstanceLevelNamespace(ExtensionContext context) {
        return Namespace.create(context.getRequiredTestInstance());
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        getInstanceLevelStore(extensionContext).put(GALEN_TEST_INFOS, new LinkedList<GalenTestInfo>());
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        Boolean dirtiesBrowser = getAnnotation(extensionContext).map(GalenTest::dirtiesBrowser).orElse(false);
        if (dirtiesBrowser) {
            Selenide.close();
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();
        return parameterType.equals(WebDriver.class) || parameterType.equals(SpecificationInterface.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();
        if (parameterType.equals(WebDriver.class)) {
            try {
                return getAndCheckWebDriver();
            } catch (WebDriverException e) {
                throw new ParameterResolutionException("Failed to resolve WebDriver parameter as WebDriver instance could not be instantiated. Please check installed browsers or your system properties 'selenide.browser' and/or 'remote'.", e);
            }
        }
        if (parameterType.equals(SpecificationInterface.class)) {
            Optional<GalenTest> annotation = getAnnotation(extensionContext);
            String specResourceRaw = annotation.map(GalenTest::specification).orElse("");
            if (specResourceRaw.isEmpty()) {
                throw new ParameterResolutionException("Unable to resolve parameter " + parameterContext.getParameter() + " as the @GalenTest annotation does not specify a specification path.");
            }
            Class<?> testClass = extensionContext.getRequiredTestClass();
            return new SpecificationImpl(testClass.getResource(specResourceRaw).getPath(), getAndCheckWebDriver());
        }
        throw new ParameterResolutionException("Failed to resolve parameter " + parameterContext.getParameter());
    }

    private static Optional<GalenTest> getAnnotation(ExtensionContext extensionContext) {
        return AnnotationUtils.findAnnotation(extensionContext.getElement(), GalenTest.class);
    }
}
