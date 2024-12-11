package com.notezkanban.lane;

import java.util.ArrayList;
import java.util.List;

import com.notezkanban.card.Card;

public class PriorityLane implements Lane {
    private String LaneId;
    private String LaneName;
    private List<Card> priorityCards;

    public PriorityLane(String LaneId, String LaneName) {
        this.LaneId = LaneId;
        this.LaneName = LaneName;
        this.priorityCards = new ArrayList<>();
    }

    @Override
    public String getLaneId() {
        return LaneId;
    }

    @Override
    public String getLaneName() {
        return LaneName;
    }

    @Override
    public List<Card> getCards() {
        return priorityCards;
    }

    @Override
    public List<Lane> getChildren() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getChildren'");
    }
}