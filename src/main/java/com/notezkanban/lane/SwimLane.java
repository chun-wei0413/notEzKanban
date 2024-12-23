package com.notezkanban.lane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.notezkanban.card.Card;
import com.notezkanban.visitor.LaneVisitor;

public class SwimLane implements Lane {
private String swimLaneId;
    private String swimLaneName;
    private List<Lane> children;
    private List<Card> cards;

    public SwimLane(String swimLaneId, String swimLaneName) {
        this.swimLaneId = swimLaneId;
        this.swimLaneName = swimLaneName;
        this.children = new ArrayList<>();
        this.cards = new ArrayList<>();
    }

    @Override
    public String getLaneId() {
        return swimLaneId;
    }
    @Override
    public String getLaneName() {
        return swimLaneName;
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
        visitor.visitSwimLane(this);
    }
}
