package com.github.mmichaelis.galen;

import com.galenframework.api.Galen;
import com.galenframework.reports.model.LayoutReport;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.Collections;

final class SpecificationImpl implements SpecificationInterface {

    private final String path;
    private WebDriver webDriver;

    SpecificationImpl(String path, WebDriver webDriver) {
        this.path = path;
        this.webDriver = webDriver;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public LayoutReport checkLayout() {
        try {
            return Galen.checkLayout(webDriver, path, Collections.emptyList());
        } catch (IOException e) {
            throw new CheckLayoutException(e);
        }
    }
}
