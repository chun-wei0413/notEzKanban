package com.notezkanban.report;

import com.notezkanban.Workflow;

public abstract class ReportGenerator<T> {

    protected final Workflow workflow;
    
    public ReportGenerator(Workflow workflow) {
        this.workflow = workflow;
    }

    public final T generateReport() {
        ReportData reportData = getCollectedData();
        return createReport(reportData);
    }

    protected abstract ReportData getCollectedData();
    
    protected abstract T createReport(ReportData data);
}
