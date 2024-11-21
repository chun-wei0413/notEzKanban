package com.notezkanban;

public class SwimLane implements Lane {
    private String laneId;
    private String laneName;

    public SwimLane(String laneId, String laneName) {
        this.laneId = laneId;
        this.laneName = laneName;
    }

    @Override
    public String getLaneId() {
        return this.laneId;
    }
}
