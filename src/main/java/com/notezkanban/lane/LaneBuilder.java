package com.notezkanban.lane;

public class LaneBuilder {
    private String laneId;
    private String laneName;
    private boolean isStage;

    public static LaneBuilder newInstance() {
        return new LaneBuilder();
    }

    public LaneBuilder laneId(String laneId) {
        this.laneId = laneId;
        return this;
    }

    public LaneBuilder laneName(String laneName) {
        this.laneName = laneName;
        return this;
    }

    public LaneBuilder stage() {
        isStage = true;
        return this;
    }

    public LaneBuilder swimLane() {
        isStage = false;
        return this;
    }

    public Lane build() {
        if (isStage) {
            return new Stage(laneId, laneName);
        } else {
            return new SwimLane(laneId, laneName);
        }
    }
}
