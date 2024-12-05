package com.notezkanban.lane;

import com.notezkanban.Visitor;
import com.notezkanban.card.Card;

import java.util.*;

public interface Lane {
    String getLaneId();
    String getLaneName();
    List<Lane> getChildren();
    void accept(Visitor visitor);

    default Iterator<Lane> iterator() {
        return getChildren().iterator();
    }

    List<Card> getCards();

    default Lane getLaneById(String laneId){
        for(Lane lane : getChildren()) {
            if (lane.getLaneId().equals(laneId)) {
                return lane;
            }
        }
        return null;
    }

    default void createStage(String stageId, String stageName) {
        Lane stage = LaneBuilder.newInstance()
            .laneId(stageId)
            .laneName(stageName)
            .stage()
            .build();

        getChildren().add(stage);
    }

    default void createSwimLane(String swimLaneId, String swimLaneName) {
        Lane swimLane = LaneBuilder.newInstance()
            .laneId(swimLaneId)
            .laneName(swimLaneName)
            .swimLane()
            .build();

        getChildren().add(swimLane);
    }

    default void addCard(Card card) {
        getCards().add(card);
    }

    default void deleteCard(Card card) {
        getCards().remove(card);
    }

    default Optional<Card> getCard(UUID id) {
        for (Card card : getCards()) {
            if (card.getId().equals(id)) {
                return Optional.of(card);
            }
        }
        return Optional.empty();
    }
}
