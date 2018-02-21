package com.github.mmichaelis.galen;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

final class Stores {
    static final String GALEN_TEST_INFOS = "galenTestInfos";
    static final String GALEN_TEST_INFO = "galenTestInfo";
    static final String GALEN_TEST_REPORT = "galenTestReport";

    private Stores() {
    }

    static Store getClassLevelStore(ExtensionContext context) {
        return context.getStore(getClassLevelNamespace(context));
    }

    private static Namespace getClassLevelNamespace(ExtensionContext context) {
        return Namespace.create(context.getRequiredTestClass());
    }

    static Store getMethodLevelStore(ExtensionContext context) {
        return context.getStore(getMethodLevelNamespace(context));
    }

    private static Namespace getMethodLevelNamespace(ExtensionContext context) {
        return Namespace.create(context.getRequiredTestInstance(), context.getRequiredTestMethod());
    }
}
