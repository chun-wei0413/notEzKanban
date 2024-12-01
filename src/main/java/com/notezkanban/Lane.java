package com.notezkanban;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Lane {
    String getLaneId();
    LaneState getState();

    default Lane getLaneById(String laneId){
        for(Lane lane : getState().getChildren()) {
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

        getState().getChildren().add(stage);
    }

    default void createSwimLane(String swimLaneId, String swimLaneName) {
        Lane swimLane = LaneBuilder.newInstance()
            .laneId(swimLaneId)
            .laneName(swimLaneName)
            .swimLane()
            .build();

        getState().getChildren().add(swimLane);
    }

    default void moveCard(Lane stage2, Card card) {
        deleteCard(card);
        stage2.addCard(card);
    }

    default void addCard(Card card) {
        getState().getCards().add(card);
    }

    default void deleteCard(Card card) {
        getState().getCards().remove(card);
    }

    default Optional<Card> getCard(UUID id) {
        for (Card card : getState().getCards()) {
            if (card.getId().equals(id)) {
                return Optional.of(card);
            }
        }
        return Optional.empty();
    }

    interface LaneState {
        List<Lane> getChildren();
        String getStageId();
        String getStageName();
        List<Card> getCards();

        static LaneState create(String stageId, String stageName) {
            return new LaneStateImpl(stageId, stageName);
        }
    }

    class LaneStateImpl implements LaneState {
        private String stageId;
        private String stageName;
        private List<Lane> children;
        private List<Card> cards;

        public LaneStateImpl(String stageId, String stageName) {
            this.stageId = stageId;
            this.stageName = stageName;
            this.children = new ArrayList<>();
            this.cards = new ArrayList<>();
        }

        @Override
        public List<Lane> getChildren() {
            return children;
        }

        @Override
        public String getStageId() {
            return stageId;
        }

        @Override
        public String getStageName() {
            return stageName;
        }

        @Override
        public List<Card> getCards() {
            return cards;
        }
    }
}
