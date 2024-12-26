package com.notezkanban.lane;

public class LaneBuilder {
    private String laneId;
    private String laneName;
    private Integer wipLimit;
    private LaneType laneType;

    private enum LaneType {
        STAGE, SWIM_LANE, EXPEDITE
    }

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

    public LaneBuilder wipLimit(int wipLimit) {
        this.wipLimit = wipLimit;
        return this;
    }

    public LaneBuilder stage() {
        this.laneType = LaneType.STAGE;
        return this;
    }

    public LaneBuilder swimLane() {
        this.laneType = LaneType.SWIM_LANE;
        return this;
    }

    public LaneBuilder expediteLane() {
        this.laneType = LaneType.EXPEDITE;
        return this;
    }

    public Lane build() {
        if (laneId == null || laneName == null) {
            throw new IllegalStateException("Lane ID and name are required");
        }

        switch (laneType) {
            case STAGE:
                return new Stage(laneId, laneName, wipLimit != null ? wipLimit : Integer.MAX_VALUE);
            case SWIM_LANE:
                return new SwimLane(laneId, laneName, wipLimit != null ? wipLimit : Integer.MAX_VALUE);
            case EXPEDITE:
                return new ExpediteLane(laneId, laneName);
            default:
                throw new IllegalStateException("Lane type must be specified");
        }
    }
}
