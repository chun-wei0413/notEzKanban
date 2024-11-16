package com.notezkanban;

public class Stage implements Lane {
    String stageId;
    String stageName;

    public Stage(String stageId, String stageName) {
        this.stageId = stageId;
        this.stageName = stageName;
    }

    @Override
    public String getLaneId() {
        return stageId;
    }
}
