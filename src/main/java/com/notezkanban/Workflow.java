package com.notezkanban;

import com.notezkanban.card.Card;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Workflow {
    String boardId;
    String workflowId;
    String workflowName;
    //only allow stages to be added to the workflow
    List<Stage> stages;

    public Workflow(String boardId, String workflowId, String workflowName) {
        checkWorkflowName(workflowName);
        this.boardId = boardId;
        this.workflowId = workflowId;
        this.workflowName = workflowName;
        stages = new ArrayList<>();
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void rename(String newWorkflowName) {
        checkWorkflowName(newWorkflowName);
        workflowName = newWorkflowName;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public Optional<Stage> getLane(String laneId) {
        for (Stage stage : stages) {
            if (stage.getLaneId().equals(laneId)) {
                return Optional.of(stage);
            }
        }
        return Optional.empty();
    }

    public void addRootStage(Stage stage) {
        stages.add(stage);
    }

    public void deleteLane(String laneId) {
        for (Stage stage : stages) {
            if (stage.getLaneId().equals(laneId)) {
                stages.remove(stage);
                break;
            }
        }
    }

    public List<Stage> getLanes() {
        return this.stages;
    }

    public void moveCard(Lane source, Lane destination, Card card){
        source.deleteCard(card);
        destination.addCard(card);
        EventBus.getInstance().publish(new DomainEvent(this.boardId, "Card moved from " + source.getLaneName() + " to " + destination.getLaneName()));
    }

    private void checkWorkflowName(String workflowName) {
        if (workflowName.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid workflow name");
        }
    }
}
