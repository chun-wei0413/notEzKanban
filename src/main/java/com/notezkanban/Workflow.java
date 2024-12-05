package com.notezkanban;

import com.notezkanban.card.Card;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.Stage;

import java.util.ArrayList;
import java.util.List;

public class Workflow {
    String workflowId;
    String workflowName;
    List<Lane> lanes;

    public Workflow(String workflowId, String workflowName) {
        this.workflowId = workflowId;
        this.workflowName = workflowName;
        lanes = new ArrayList<>();
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public Lane getLane(String laneId) {
        for (Lane lane : lanes) {
            if (lane.getLaneId().equals(laneId)) {
                return lane;
            }
        }
        return null;
    }

    public void addLane(Lane lane) {
        if (!checkRootStageIsStage(lane)) {
            throw new RuntimeException("RootStage must be stage");
        }
        lanes.add(lane);
    }

    public List<Lane> getLanes() {
        return this.lanes;
    }

    public void moveCard(Lane source, Lane destination, Card card){
        source.deleteCard(card);
        destination.addCard(card);
    }

    private boolean checkRootStageIsStage(Lane lane) {
        if (lanes.isEmpty()) {
            return lane instanceof Stage;
        }
        return true;
    }
}
