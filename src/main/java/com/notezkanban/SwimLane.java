package com.notezkanban;

public class SwimLane implements Lane {
    private final LaneState state;

    public SwimLane(String laneId, String laneName) {
        state = LaneState.create(laneId, laneName);
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
