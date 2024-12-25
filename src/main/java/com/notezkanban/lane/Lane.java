package com.notezkanban.lane;

import com.notezkanban.card.Card;
import com.notezkanban.card.CardType;
import com.notezkanban.iterator.DFSLaneIterator;
import com.notezkanban.lane.exception.LaneException;
import com.notezkanban.visitor.LaneVisitor;

import java.util.*;

public interface Lane {
    String getLaneId();
    String getLaneName();
    List<Lane> getChildren();

    Iterator<Lane> iterator();

    default Iterator<Lane> dfsIterator() {
        return new DFSLaneIterator(this);
    }

    List<Card> getCards();
    <T>void accept(LaneVisitor<T> visitor);

    default Lane getLaneById(String laneId){
        for(Lane lane : getChildren()) {
            if (lane.getLaneId().equals(laneId)) {
                return lane;
            }
        }
        return null;
    }

    //notAllowStageAndSwimLaneInSameLane
    default void createStage(String stageId, String stageName) {
        for (Lane lane : getChildren()) {
            if (lane instanceof SwimLane) {
                throw new LaneException("Cannot add a stage to a lane that contains swim lanes.");
            }
        }

        Lane stage = LaneBuilder.newInstance()
                .laneId(stageId)
                .laneName(stageName)
                .stage()
                .build();

        getChildren().add(stage);
    }

    //notAllowStageAndSwimLaneInSameLane
    default void createSwimLane(String swimLaneId, String swimLaneName) {
        for (Lane lane : getChildren()) {
            if (lane instanceof Stage) {
                throw new LaneException("Cannot add a stage to a lane that contains swim lanes.");
            }
        }

        Lane swimLane = LaneBuilder.newInstance()
            .laneId(swimLaneId)
            .laneName(swimLaneName)
            .swimLane()
            .build();

        getChildren().add(swimLane);
    }

    default void createCard(String description, CardType type, String boardId) {
        if (!getChildren().isEmpty()) {
            throw new LaneException("Cannot add cards to non-leaf lanes.");
        }

        Card card = new Card(description, type, boardId);
        addCard(card);
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


    default int getExpediteCardCount() {
        int count = (int) getCards().stream()
                .filter(card -> card.getType() == CardType.Expedite)
                .count();

        for (Lane child : getChildren()) {
            count += child.getExpediteCardCount();
        }

        return count;
    }

}
