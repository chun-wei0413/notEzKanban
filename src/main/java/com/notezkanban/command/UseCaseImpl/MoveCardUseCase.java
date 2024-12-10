package com.notezkanban.command.UseCaseImpl;

import com.notezkanban.Workflow;
import com.notezkanban.card.Card;
import com.notezkanban.command.UseCase;
import com.notezkanban.lane.Lane;

public class MoveCardUseCase implements UseCase {
    private Workflow workflow;
    private Lane oldLane;
    private Lane newLane;
    private Card card;

    public MoveCardUseCase(Workflow workflow, String oldLaneId, String newLaneId, Card card) {
        this.workflow = workflow;
        this.oldLane = oldLane;
        this.newLane = newLane;
        this.card = card;
    }

    @Override
    public void execute() {
        if (workflow.getLane(oldLane.getLaneId()).isPresent() && workflow.getLane(newLane.getLaneId()).isPresent()) {
            workflow.moveCard(oldLane, newLane, card);
        }
    }
}
