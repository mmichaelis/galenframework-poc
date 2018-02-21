package com.github.mmichaelis.galen;

import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.TestReport;
import com.galenframework.reports.model.FileTempStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.lang.invoke.MethodHandles.lookup;

final class GalenUtil {
    private static final Logger LOG = LoggerFactory.getLogger(lookup().lookupClass());

    private GalenUtil() {
    }

    static void cleanData(List<GalenTestInfo> testInfos) {
        for (GalenTestInfo testInfo : testInfos) {
            TestReport testReport = testInfo.getReport();
            if (testReport != null) {
                cleanFileStorage(testReport);
            }
        }
    }

    private static void cleanFileStorage(TestReport testReport) {
        try {
            FileTempStorage storage = testReport.getFileStorage();
            if (storage != null) {
                storage.cleanup();
            }
        } catch (Exception e) {
            LOG.error("Unkown error during report cleaning", e);
        }
    }
}
