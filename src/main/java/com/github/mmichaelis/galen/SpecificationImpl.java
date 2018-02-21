package com.github.mmichaelis.galen;

import com.galenframework.api.Galen;
import com.galenframework.config.GalenConfig;
import com.galenframework.config.GalenProperty;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.validation.ValidationResult;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

final class SpecificationImpl implements Specification {
    private static final String ERROR_INDENTATION = "  ";

    private final String path;
    private final WebDriver webDriver;
    private final GalenTestInfo testInfo;
    private final AtomicBoolean gotChecked = new AtomicBoolean();

    SpecificationImpl(String path, WebDriver webDriver, GalenTestInfo testInfo) {
        this.path = path;
        this.webDriver = webDriver;
        this.testInfo = testInfo;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void checkLayout() {
        checkLayout(emptyList());
    }

    public void checkLayout(List<String> includedTags) {
        checkLayout(new SectionFilter(includedTags, emptyList()), new Properties(), Collections.emptyMap());
    }

    public void checkLayout(SectionFilter sectionFilter, Properties properties, Map<String, Object> jsVariables) {
        gotChecked.set(true);
        // Take screenshot of whole page. Use temporary folder to be deleted later on (see testreport cleanup).
        boolean originalScreenshotFullpage = GalenConfig.getConfig().getBooleanProperty(GalenProperty.SCREENSHOT_FULLPAGE);
        GalenConfig.getConfig().setProperty(GalenProperty.SCREENSHOT_FULLPAGE, "true");
        try {
            String title = format("Check layout %s", path);
            LayoutReport layoutReport = Galen.checkLayout(webDriver, path, sectionFilter, properties, jsVariables);
            testInfo.getReport().layout(layoutReport, title);
            if (layoutReport.errors() > 0) {
                Assertions.fail(createMessage(path, layoutReport, sectionFilter));
            }
        } catch (IOException e) {
            throw new CheckLayoutException(e);
        } finally {
            GalenConfig.getConfig().setProperty(GalenProperty.SCREENSHOT_FULLPAGE, Boolean.toString(originalScreenshotFullpage));
        }
    }

    public boolean isChecked() {
        return gotChecked.get();
    }

    private static String createMessage(String specPath, LayoutReport layoutReport, SectionFilter sectionFilter) {
        try {
            StringBuilder messageBuilder = new StringBuilder()
                    .append(specPath);

            if (sectionFilter != null) {
                if (sectionFilter.getIncludedTags() != null && !sectionFilter.getIncludedTags().isEmpty()) {
                    messageBuilder.append(", tags: ").append(sectionFilter.getIncludedTags()).append("\n");
                }
                if (sectionFilter.getExcludedTags() != null && !sectionFilter.getExcludedTags().isEmpty()) {
                    messageBuilder.append(", excludedTags: ").append(sectionFilter.getExcludedTags()).append("\n");
                }
            }


            messageBuilder.append(collectAllErrors(layoutReport.getValidationErrorResults()));
            return messageBuilder.toString();

        } catch (Exception ex) {
            return specPath;
        }

    }

    private static StringBuilder collectAllErrors(List<ValidationResult> validationErrorResults) {
        return collectAllErrors(validationErrorResults, ERROR_INDENTATION);
    }

    private static StringBuilder collectAllErrors(List<ValidationResult> validationErrorResults, String indentation) {
        StringBuilder builder = new StringBuilder();
        if (validationErrorResults != null) {
            String childIndentation = indentation + ERROR_INDENTATION;

            for (ValidationResult validationResult : validationErrorResults) {
                for (String errorMessage : validationResult.getError().getMessages()) {
                    builder.append(indentation)
                            .append("- ")
                            .append(errorMessage);

                    if (validationResult.getSpec() != null && validationResult.getSpec().getPlace() != null) {
                        builder.append(" (").append(validationResult.getSpec().getPlace().toPrettyString()).append(")");
                    }
                    builder.append("\n");
                }

                builder.append(collectAllErrors(validationResult.getChildValidationResults(), childIndentation));
            }
        }
        return builder;
    }
}
