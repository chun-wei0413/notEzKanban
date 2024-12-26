package com.notezkanban.lane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.notezkanban.card.Card;
import com.notezkanban.card.CardType;
import com.notezkanban.lane.exception.LaneException;
import com.notezkanban.visitor.LaneVisitor;

public class Stage implements Lane {
    private String stageId;
    private String stageName;
    private List<Lane> children;
    private List<Card> cards;
    private int wipLimit;

    public Stage(String stageId, String stageName, int wipLimit) {
        this.stageId = stageId;
        this.stageName = stageName;
        this.children = new ArrayList<>();
        this.cards = new ArrayList<>();
        this.wipLimit = wipLimit;
    }

    @Override
    public String getLaneId() {
        return stageId;
    }
    @Override
    public String getLaneName() {
        return stageName;
    }

    @Override
    public List<Lane> getChildren() {
        return children;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    @Override
    public void createCard(String description, CardType type, String boardId) {
        if (!getChildren().isEmpty()) {
            throw new LaneException("Cannot add cards to non-leaf lanes.");
        }

        Card card = new Card(description, type, boardId);
        addCard(card);
    }

    private void addCard(Card card) {
        if (cards.size() >= wipLimit) {
            throw new LaneException("WIP limit reached.");
        }

        cards.add(card);
    }

    @Override
    public Iterator<Lane> iterator() {
        return children.iterator();
    }

    @Override
    public void accept(LaneVisitor visitor) {
        visitor.visitStage(this);
    }

    @Override
    public int getWipLimit() {
        return wipLimit;
    }

    @Override
    public void setWipLimit(int limit) {
        this.wipLimit = limit;
    }
}
