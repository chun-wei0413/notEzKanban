package com.notezkanban.chart;

import java.util.HashMap;
import java.util.Map;

public class Chart {
    private final String title;
    private final Map<String, Integer> distribution;
    private final String workflowName;

    public Chart(ChartData data) {
        this.workflowName = data.getWorkflowName();
        this.distribution = data.getDistribution();
        this.title = generateTitle();
    }

    private String generateTitle() {
        return "Distribution Chart - " + workflowName;
    }

    // Getters for testing and data access
    public String getTitle() {
        return title;
    }

    public Map<String, Integer> getDistribution() {
        return new HashMap<>(distribution); // 返回副本以保护内部数据
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public int getCardCount(String stageName) {
        return distribution.getOrDefault(stageName, 0);
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
