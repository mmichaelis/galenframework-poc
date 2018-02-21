package com.github.mmichaelis.galen;

import com.codeborne.selenide.Selenide;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.github.mmichaelis.galen.Parameters.resolveSpecificationParameter;
import static com.github.mmichaelis.galen.Parameters.resolveWebDriverParameter;
import static com.github.mmichaelis.galen.Stores.GALEN_TEST_INFO;
import static com.github.mmichaelis.galen.Stores.GALEN_TEST_INFOS;
import static com.github.mmichaelis.galen.Stores.GALEN_TEST_REPORT;
import static com.github.mmichaelis.galen.Stores.getClassLevelStore;
import static com.github.mmichaelis.galen.Stores.getMethodLevelStore;
import static java.util.Collections.synchronizedList;

public class GalenExtension implements BeforeAllCallback, AfterAllCallback, AfterEachCallback, BeforeEachCallback, ParameterResolver {

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        getClassLevelStore(extensionContext).put(GALEN_TEST_INFOS, synchronizedList(new ArrayList<GalenTestInfo>()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterAll(ExtensionContext extensionContext) {
        List<GalenTestInfo> testInfos = (List<GalenTestInfo>) getClassLevelStore(extensionContext).remove(GALEN_TEST_INFOS, List.class);
        try {
            new HtmlReportBuilder().build(testInfos, "target/galen-html-reports");
        } catch (IOException e) {
            throw new GalenExtensionException("Failed to store Galen HTML report.", e);
        } finally {
            GalenUtil.cleanData(testInfos);
        }
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        Store store = getMethodLevelStore(extensionContext);
        GalenTestInfo testInfo = GalenTestInfo.fromString(extensionContext.getDisplayName());
        testInfo.setStartedAt(new Date());
        store.put(GALEN_TEST_INFO, testInfo);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        handleDirtiesBrowser(extensionContext);
        addTestInfoToResult(extensionContext);
    }

    @SuppressWarnings("unchecked")
    private static void addTestInfoToResult(ExtensionContext extensionContext) {
        Store store = getMethodLevelStore(extensionContext);
        GalenTestInfo testInfo = store.remove(GALEN_TEST_INFO, GalenTestInfo.class);
        testInfo.setEndedAt(new Date());
        List<GalenTestInfo> testInfos = (List<GalenTestInfo>) getClassLevelStore(extensionContext).get(GALEN_TEST_INFOS, List.class);
        testInfos.add(testInfo);
        store.remove(GALEN_TEST_REPORT);
    }

    private static void handleDirtiesBrowser(ExtensionContext extensionContext) {
        Boolean dirtiesBrowser = Annotations.getAnnotation(extensionContext).map(GalenTest::dirtiesBrowser).orElse(false);
        if (dirtiesBrowser) {
            Selenide.close();
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return Parameters.supportsParameter(parameterContext);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (Parameters.isParameterOfType(parameterContext, WebDriver.class)) {
            return resolveWebDriverParameter();
        }
        if (Parameters.isParameterOfType(parameterContext, Specification.class)) {
            return resolveSpecificationParameter(parameterContext, extensionContext);
        }
        if (Parameters.isParameterOfType(parameterContext, GalenTestInfo.class)) {
            return getMethodLevelStore(extensionContext).get(GALEN_TEST_INFO, GalenTestInfo.class);
        }
        throw new ParameterResolutionException("Failed to resolve parameter " + parameterContext.getParameter());
    }

}
