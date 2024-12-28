package com.notezkanban.report;

import java.util.Map;
import java.util.UUID;

public class ReportData {
    private final String workflowName;
    private final Map<String, Integer> distribution;
    private final Map<String, Map<UUID, Double>> cycleTimeData;

    public ReportData(String workflowName,
                      Map<String, Integer> distribution,
                      Map<String, Map<UUID, Double>> cycleTimeData) {
        this.workflowName = workflowName;
        this.distribution = distribution;
        this.cycleTimeData = cycleTimeData;
    }

    public static ReportData forDistribution(String workflowName, Map<String, Integer> distribution) {
        return new ReportData(workflowName, distribution, null);
    }

    public static ReportData forCycleTime(String workflowName, Map<String, Map<UUID, Double>> cycleTimeData) {
        return new ReportData(workflowName, null, cycleTimeData);
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public Map<String, Integer> getDistribution() {
        if (distribution == null) {
            throw new UnsupportedOperationException("Distribution data is not available.");
        }
        return distribution;
    }

    public Map<String, Map<UUID, Double>> getCycleTimeData() {
        if (cycleTimeData == null) {
            throw new UnsupportedOperationException("Cycle time data is not available.");
        }
        return cycleTimeData;
    }
}


