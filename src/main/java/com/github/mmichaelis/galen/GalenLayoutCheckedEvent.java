package com.github.mmichaelis.galen;

import com.galenframework.reports.model.LayoutReport;

import java.util.List;

public interface GalenLayoutCheckedEvent {
    LayoutReport getLayoutReport();
    String getTestName();
    List<String> getGroups();
}
