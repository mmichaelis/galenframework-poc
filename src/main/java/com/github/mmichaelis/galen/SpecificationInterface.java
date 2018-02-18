package com.github.mmichaelis.galen;

import com.galenframework.reports.model.LayoutReport;

public interface SpecificationInterface {
    String getPath();

    LayoutReport checkLayout();
}
