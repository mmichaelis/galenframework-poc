package com.github.mmichaelis.galen;

import com.galenframework.api.Galen;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.validation.ValidationResult;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;

class GalenExtensionTest {
    @BeforeAll
    static void setUpAll() {
        System.setProperty("selenide.browser", "chrome");
        System.setProperty("selenide.baseUrl", "https://2017.cssconf.eu");
    }

    @DisplayName("CSS Conf Mainpage Layout")
    @Nested
    class MainpageLayoutTest {
        @DisplayName("Check Top Bar Layout")
        @GalenTest(specification = "common/top-bar.gspec")
        @SectionFilter({
                @SectionTag("mobile"),
                @SectionTag("desktop")
        })
        @Property(key = "hurz", value = "schnurz")
        @Property(key = "hurz1", value = "schnurz2")
        @JavaScriptVariable(name = "lorem", value = "ipsum")
        void checkTopBarLayout() throws IOException {
            open("/");
            // Screenshot: Testname und Zeitstempel
            // Wie sammeln wir die Ergebnisse? Was sind die Listener?
/*
            Galen.checkLayout((WebDriver) null, null, new com.galenframework.speclang2.pagespec.SectionFilter(Collections.emptyList(),
                            Galen.EMPTY_TAGS),
                    Galen.EMPTY_PROPERTIES, Galen.EMPTY_VARS, Galen.EMPTY_SCREENSHOT_FILE, Galen.EMPTY_VALIDATION_LISTENER);
*/
        }
    }

    @GalenTest(specification = "example.gspec")
    void proof_of_concept(WebDriver webDriver, SpecificationInterface specification) throws Exception {
        open("/");
        // Galen.checkLayout(null, null, Collections.emptyList());
        LayoutReport report = specification.checkLayout();
        List<ValidationResult> list = report.getValidationErrorResults();

        List<GalenTestInfo> tests = new LinkedList<GalenTestInfo>();

        // Use DisplayName
// Creating an object that will contain the information about the test
        GalenTestInfo test = GalenTestInfo.fromString("Login page on mobile device test", Arrays.asList("lorem", "ipsum"));

// Adding layout report to the test report
        test.getReport().layout(report, "check layout on mobile device");
        tests.add(test);


// Exporting all test reports to html
        new HtmlReportBuilder().build(tests, "target/galen-html-reports");

        org.assertj.core.api.Assertions.assertThat(list).isEmpty();
    }
}
