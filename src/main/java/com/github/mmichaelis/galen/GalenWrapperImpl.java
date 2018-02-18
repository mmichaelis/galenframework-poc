package com.github.mmichaelis.galen;

import com.galenframework.api.Galen;
import com.galenframework.reports.model.LayoutReport;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.Collections;

public class GalenWrapperImpl implements GalenWrapper {
    private WebDriver driver;
    private String specificationPath;

    @Override
    public void checkLayout() throws IOException {
        LayoutReport layoutReport = Galen.checkLayout(driver, specificationPath, Collections.emptyList());

    }
}
