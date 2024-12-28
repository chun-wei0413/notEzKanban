package com.notezkanban.chart;

import java.util.Map;

import com.notezkanban.Workflow;

public abstract class ChartGenerator<T extends Chart> {

    protected final Workflow workflow;
    
    public ChartGenerator(Workflow workflow) {
        this.workflow = workflow;
    }

    public final T generateChart() {
        ChartData chartData = getCollectedData();
        return createChart(chartData);
    }

    protected abstract ChartData getCollectedData();
    
    protected abstract T createChart(ChartData data);
}
