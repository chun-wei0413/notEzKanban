package com.notezkanban;

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
        // Add stage to workflow
        lanes.add(lane);
    }
}
