package com.notezkanban.report;

import java.util.HashMap;
import java.util.Map;

public class DistributionReport {
    private final String title;
    private final String workflowName;
    private final Map<String, Integer> distribution;

    public DistributionReport(ReportData data) {
        this.workflowName = data.getWorkflowName();
        this.distribution = data.getDistribution();
        this.title = generateTitle();
    }

    private String generateTitle() {
        return "Distribution Chart - " + workflowName;
    }

    public int getTotalCards() {
        return distribution.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(title).append("\n");
        sb.append("=".repeat(title.length())).append("\n\n");
        
        distribution.forEach((stage, count) -> 
            sb.append(String.format("%s: %d cards\n", stage, count))
        );
        
        sb.append("\nTotal cards: ").append(getTotalCards());
        return sb.toString();
    }
}
