package com.notezkanban.report;

import java.util.Map;
import java.util.UUID;

public class CycleTimeReport {
    private final String title;
    private final String workflowName;
    private final Map<String, Map<UUID, Double>> cycleTimeData;

    public CycleTimeReport(ReportData data) {
        this.workflowName = data.getWorkflowName();
        this.cycleTimeData = data.getCycleTimeData();
        this.title = generateTitle();
    }

    private String generateTitle() {
        return "Cycle Time Chart - " + workflowName;
    }

    public Map<String, Map<UUID, Double>> getCycleTimeData() {
        return cycleTimeData;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cycle Time Report - ").append(workflowName).append("\n");
        sb.append("=".repeat(40)).append("\n");

        for (Map.Entry<String, Map<UUID, Double>> entry : cycleTimeData.entrySet()) {
            sb.append("Lane: ").append(entry.getKey()).append("\n");
            for (Map.Entry<UUID, Double> cardEntry : entry.getValue().entrySet()) {
                sb.append("  Card ID: ").append(cardEntry.getKey())
                        .append(", Cycle Time: ").append(cardEntry.getValue()).append(" days\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
