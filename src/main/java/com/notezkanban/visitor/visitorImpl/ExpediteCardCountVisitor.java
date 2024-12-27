package com.notezkanban.visitor.visitorImpl;

import com.notezkanban.card.CardType;
import com.notezkanban.lane.ExpediteLane;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.Stage;
import com.notezkanban.lane.SwimLane;
import com.notezkanban.visitor.LaneVisitor;

public class ExpediteCardCountVisitor implements LaneVisitor<Integer> {
    private int expediteCardCount = 0;

    @Override
    public void visitStage(Stage stage) {
        countExpediteCards(stage);
        stage.getChildren().forEach(child -> child.accept(this));
    }

    @Override
    public void visitSwimLane(SwimLane swimLane) {
        countExpediteCards(swimLane);
        swimLane.getChildren().forEach(child -> child.accept(this));
    }

    @Override
    public void visitExpediteLane(ExpediteLane expediteLane) {
        countExpediteCards(expediteLane);
    }

    private void countExpediteCards(Lane lane) {
        expediteCardCount += lane.getCards().stream()
                .filter(card -> card.getType() == CardType.Expedite)
                .count();
    }

    @Override
    public Integer getResult() {
        return expediteCardCount;
    }
}
