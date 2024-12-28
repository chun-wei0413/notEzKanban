package com.notezkanban.chart;

import java.util.HashMap;
import java.util.Map;

import com.notezkanban.Workflow;
import com.notezkanban.lane.Stage;
import com.notezkanban.visitor.visitorImpl.TotalCardVisitor;

public class DistributionChartGenerator extends ChartGenerator<Chart> {
    private Map<String, Integer> cardDistribution;

    public DistributionChartGenerator(Workflow workflow) {
        super(workflow);
        this.cardDistribution = new HashMap<>();
    }

    @Override
    protected ChartData getCollectedData() {
        return workflow.collectDistributionChartData();
    }

    @Override
    protected Chart createChart(ChartData data) {
        return new Chart(data);
    }
}
