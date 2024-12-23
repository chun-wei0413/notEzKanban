package com.notezkanban.lane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.notezkanban.card.Card;
import com.notezkanban.visitor.LaneVisitor;

public class Stage implements Lane {
    private String stageId;
    private String stageName;
    private List<Lane> children;
    private List<Card> cards;

    public Stage(String stageId, String stageName) {
        this.stageId = stageId;
        this.stageName = stageName;
        this.children = new ArrayList<>();
        this.cards = new ArrayList<>();
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
    public Iterator<Lane> iterator() {
        return children.iterator();
    }

    @Override
    public void accept(LaneVisitor visitor) {
        visitor.visitStage(this);
    }
}
