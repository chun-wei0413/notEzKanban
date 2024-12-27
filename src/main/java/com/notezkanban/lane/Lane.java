package com.notezkanban.lane;

import com.notezkanban.card.Card;
import com.notezkanban.card.CardType;
import com.notezkanban.iterator.DFSLaneIterator;
import com.notezkanban.lane.exception.LaneException;
import com.notezkanban.visitor.LaneVisitor;

import java.util.*;

public interface Lane {
    //tested
    String getLaneId();
    //tested
    String getLaneName();
    //tested
    List<Lane> getChildren();
    //tested
    int getWipLimit();
    //tested
    void setWipLimit(int limit);
    //tested
    Iterator<Lane> iterator();
    //tested
    default Iterator<Lane> dfsIterator() {
        return new DFSLaneIterator(this);
    }

    //tested
    default Lane getLaneById(String laneId){
        for(Lane lane : getChildren()) {
            if (lane.getLaneId().equals(laneId)) {
                return lane;
            }
        }
        return null;
    }

    //tested
    //notAllowStageAndSwimLaneInSameLane
    default void createStage(String stageId, String stageName, int wipLimit) {
        for (Lane lane : getChildren()) {
            if (lane instanceof SwimLane) {
                throw new LaneException("Cannot add a stage to a lane that contains swim lanes.");
            }
        }

        Lane stage = LaneBuilder.newInstance()
                .laneId(stageId)
                .laneName(stageName)
                .wipLimit(wipLimit)
                .stage()
                .build();

        getChildren().add(stage);
    }

    //tested
    //notAllowStageAndSwimLaneInSameLane
    default void createSwimLane(String swimLaneId, String swimLaneName, int wipLimit) {
        for (Lane lane : getChildren()) {
            if (lane instanceof Stage) {
                throw new LaneException("Cannot add a stage to a lane that contains swim lanes.");
            }
        }

        Lane swimLane = LaneBuilder.newInstance()
            .laneId(swimLaneId)
            .laneName(swimLaneName)
            .wipLimit(wipLimit)
            .swimLane()
            .build();

        getChildren().add(swimLane);
    }

    //tested
    default void createExpediteLane(String expediteLaneId, String expediteLaneName) {
        Lane expediteLane = LaneBuilder.newInstance()
            .laneId(expediteLaneId)
            .laneName(expediteLaneName)
            .expediteLane()
            .build();

        getChildren().add(expediteLane);
    }


    //tested
    List<Card> getCards();

    //tested
    void createCard(String description, CardType type, String boardId);

    //tested
    default void deleteCard(Card card) {
        getCards().remove(card);
    }

    //tested
    default Optional<Card> getCard(UUID id) {
        for (Card card : getCards()) {
            if (card.getId().equals(id)) {
                return Optional.of(card);
            }
        }
        return Optional.empty();
    }

    <T>void accept(LaneVisitor<T> visitor);
}
