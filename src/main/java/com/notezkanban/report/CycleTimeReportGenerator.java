package com.notezkanban.report;

import com.notezkanban.Workflow;

public class CycleTimeReportGenerator extends ReportGenerator<CycleTimeReport> {

    public CycleTimeReportGenerator(Workflow workflow) {
        super(workflow);
    }

    @Override
    protected ReportData getCollectedData() {
        return workflow.collectCycleTimeData();
    }

    @Override
    protected CycleTimeReport createReport(ReportData data) {
        return new CycleTimeReport(data);
    }
}
