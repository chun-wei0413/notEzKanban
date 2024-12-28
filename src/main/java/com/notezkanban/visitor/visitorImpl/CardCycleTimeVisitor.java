package com.notezkanban.visitor.visitorImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;


import com.notezkanban.card.Card;
import com.notezkanban.lane.ExpediteLane;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.Stage;
import com.notezkanban.lane.SwimLane;
import com.notezkanban.visitor.LaneVisitor;

public class CardCycleTimeVisitor implements LaneVisitor<Map<String, Map<UUID, Double>>> {

    private final Map<String, Map<UUID, Double>> cycleTimeData = new HashMap<>();

    @Override
    public void visitStage(Stage stage) {
        Map<UUID, Double> cardCycleTimes = new HashMap<>();
        for (Card card : stage.getCards()) { // 假設 Stage 有 getCards() 方法返回所有卡片
            double cycleTime = card.getCycleTime(stage.getLaneId());
            cardCycleTimes.put(card.getId(), cycleTime);
        }
        cycleTimeData.put(stage.getLaneName(), cardCycleTimes);

        // 遍歷子 Lane
        Iterator<Lane> it = stage.iterator();
        while (it.hasNext()) {
            it.next().accept(this);
        }
    }

    @Override
    public void visitSwimLane(SwimLane swimLane) {
        Map<UUID, Double> cardCycleTimes = new HashMap<>();
        for (Card card : swimLane.getCards()) {
            double cycleTime = card.getCycleTime(swimLane.getLaneId());
            cardCycleTimes.put(card.getId(), cycleTime);
        }
        cycleTimeData.put(swimLane.getLaneName(), cardCycleTimes);

        // 遍歷子 Lane
        Iterator<Lane> it = swimLane.iterator();
        while (it.hasNext()) {
            it.next().accept(this);
        }
    }

    @Override
    public void visitExpediteLane(ExpediteLane expediteLane) {
        Map<UUID, Double> cardCycleTimes = new HashMap<>();
        for (Card card : expediteLane.getCards()) {
            double cycleTime = card.getCycleTime(expediteLane.getLaneId());
            cardCycleTimes.put(card.getId(), cycleTime);
        }
        cycleTimeData.put(expediteLane.getLaneName(), cardCycleTimes);
    }

    @Override
    public Map<String, Map<UUID, Double>> getResult() {
        return cycleTimeData;
    }
}