package com.notezkanban.report;

import com.notezkanban.Workflow;

public class DistributionReportGenerator extends ReportGenerator<DistributionReport> {

    public DistributionReportGenerator(Workflow workflow) {
        super(workflow);
    }

    @Override
    protected ReportData getCollectedData() {
        return workflow.collectDistributionChartData();
    }

    @Override
    protected DistributionReport createReport(ReportData data) {
        return new DistributionReport(data);
    }
}
