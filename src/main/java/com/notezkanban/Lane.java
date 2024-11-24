package com.notezkanban;

import java.util.ArrayList;
import java.util.List;

public interface Lane {
    String getLaneId();
    LaneState getState();

    default Lane getLaneById(String laneId){
        for(Lane lane : getState().getChildren()) {
            if (lane.getLaneId().equals(laneId)) {
                return lane;
            }
        }
        return null;
    }

    default void createStage(String stageId, String stageName) {
        Lane stage = LaneBuilder.newInstance()
            .laneId(stageId)
            .laneName(stageName)
            .stage()
            .build();

        getState().getChildren().add(stage);
    }

    default void createSwimLane(String swimLaneId, String swimLaneName) {
        Lane swimLane = LaneBuilder.newInstance()
            .laneId(swimLaneId)
            .laneName(swimLaneName)
            .swimLane()
            .build();

        getState().getChildren().add(swimLane);
    }

    interface LaneState {
        List<Lane> getChildren();
        String getStageId();
        String getStageName();

        static LaneState create(String stageId, String stageName) {
            return new LaneStateImpl(stageId, stageName);
        }
    }

    class LaneStateImpl implements LaneState {
        private String stageId;
        private String stageName;
        private List<Lane> children;

        public LaneStateImpl(String stageId, String stageName) {
            this.stageId = stageId;
            this.stageName = stageName;
            this.children = new ArrayList<>();
        }

        @Override
        public List<Lane> getChildren() {
            return children;
        }

        @Override
        public String getStageId() {
            return stageId;
        }

        @Override
        public String getStageName() {
            return stageName;
        }
    }
}
