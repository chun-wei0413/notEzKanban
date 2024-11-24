package com.notezkanban;

public class Stage implements Lane {
    private final LaneState state;

    public Stage(String stageId, String stageName) {
        this.state = LaneState.create(stageId, stageName);

    }

    @Override
    public LaneState getState() {
        return state;
    }

    @Override
    public String getLaneId() {
        return getState().getStageId();
    }
}
