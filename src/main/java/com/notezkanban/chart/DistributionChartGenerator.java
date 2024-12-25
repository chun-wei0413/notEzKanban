package com.notezkanban.chart;

import java.util.HashMap;
import java.util.Map;

import com.notezkanban.Workflow;
import com.notezkanban.lane.Stage;

public class DistributionChartGenerator extends ChartGenerator<Chart> {
    private Map<String, Integer> cardDistribution;

    public DistributionChartGenerator(Workflow workflow) {
        super(workflow);
    }

    @Override
    protected void collectData() {
        cardDistribution = new HashMap<>();
        for (Stage stage : workflow.getLanes()) {
            cardDistribution.put(
                stage.getLaneName(), 
                stage.getCards().size()
            );
        }
    }

    @Override
    protected ChartData getCollectedData() {
        return new ChartData(
            workflow.getWorkflowName(),
            cardDistribution
        );
    }

    @Override
    protected Chart createChart(ChartData data) {
        return new Chart(data);
    }
}
