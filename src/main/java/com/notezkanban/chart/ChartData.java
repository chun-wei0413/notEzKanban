package com.notezkanban.chart;

import java.util.Map;

public class ChartData {
    private final String workflowName;
    private final Map<String, Integer> distribution;

    public ChartData(String workflowName, Map<String, Integer> distribution) {
        this.workflowName = workflowName;
        this.distribution = distribution;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public Map<String, Integer> getDistribution() {
        return distribution;
    }
}
