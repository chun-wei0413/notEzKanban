package com.notezkanban.lane;

import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

import com.notezkanban.card.Card;
import com.notezkanban.card.CardType;
import com.notezkanban.lane.exception.LaneException;
import com.notezkanban.visitor.LaneVisitor;

public class ExpediteLane implements Lane {
    private final String laneId;
    private final String laneName;
    private final List<Card> cards = new ArrayList<>();

    public ExpediteLane(String laneId, String laneName) {
        this.laneId = laneId;
        this.laneName = laneName;
    }

    @Override
    public String getLaneId() {
        return laneId;
    }

    @Override
    public String getLaneName() {
        return laneName;
    }

    @Override
    public List<Lane> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    @Override
    public int getWipLimit() {
        return 1; // 固定為 1
    }

    @Override
    public void setWipLimit(int limit) {
        throw new UnsupportedOperationException("Cannot change WIP limit for Expedite Lane");
    }

    @Override
    public void createStage(String stageId, String stageName, int wipLimit) {
        throw new LaneException("Cannot create stage in Expedite Lane");
    }

    @Override
    public void createSwimLane(String swimLaneId, String swimLaneName, int wipLimit) {
        throw new LaneException("Cannot create swim lane in Expedite Lane");
    }

    @Override
    public void createExpediteLane(String expediteLaneId, String expediteLaneName) {
        throw new LaneException("Cannot create expedite lane in Expedite Lane");
    }

    @Override
    public void accept(LaneVisitor visitor) {
        visitor.visitExpediteLane(this);
    }

    @Override
    public void createCard(String description, CardType type, String boardId) {
        if (type != CardType.Expedite) {
            throw new LaneException("Expedite Lane can only accept Expedite cards");
        }
        if (cards.size() >= getWipLimit()) {
            throw new LaneException("WIP limit exceeded");
        }
        addCard(new Card(description, type, boardId));
    }

    private void addCard(Card card) {
        cards.add(card);
    }
}