package com.notezkanban.lane;

import java.util.Iterator;
import java.util.List;

import com.notezkanban.card.Card;
import com.notezkanban.card.CardType;
import com.notezkanban.lane.exception.LaneException;
import com.notezkanban.visitor.LaneVisitor;

public class ExpediteLane extends Stage {

    public ExpediteLane(String stageId, String stageName) {
        super(stageId, stageName);
    }

    @Override
    public void addCard(Card card) {
        if (card.getType() != CardType.Expedite) {
            throw new LaneException("Only expedite cards can be added to an expedite lane.");
        }
        super.addCard(card);
    }

    @Override
    public void accept(LaneVisitor visitor) {
        // TODO Auto-generated method stub
    }
}