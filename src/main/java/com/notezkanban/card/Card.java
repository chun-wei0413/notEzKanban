package com.notezkanban.card;

import com.notezkanban.card.exception.CardException;

import java.util.*;

public class Card {
    private String description;
    private CardType type;
    private String boardId;
    private UUID cardId;
    private final Map<String, List<Long>> stageEntryTimes; // 记录每个阶段的进入时间
    private final Map<String, List<Long>> stageExitTimes;  // 记录每个阶段的退出时间

    public Card(String description, CardType type, String boardId) {
        if (isCardInvalid(description)) {
            throw new CardException("Invalid card create");
        }
        this.description = description;
        this.type = type;
        this.boardId = boardId;
        this.cardId = UUID.randomUUID();
        this.stageEntryTimes = new HashMap<>();
        this.stageExitTimes = new HashMap<>();
    }

    private boolean isCardInvalid(String description) {
        return description.trim().isEmpty();
    }

    public CardType getType() {
        return type;
    }

    public String getBoardId() {
        return boardId;
    }

    public String getDescription() {
        return description;
    }

    public UUID getId() {
        return cardId;
    }

    public void changeDescription(String newDescription) {
        if (isCardInvalid(newDescription)) {
            throw new CardException("Invalid card description");
        }
        description = newDescription;
    }

    public void enterStage(String stageId) {
        stageEntryTimes.putIfAbsent(stageId, new ArrayList<>());
        stageEntryTimes.get(stageId).add(System.currentTimeMillis());
    }

    public void exitStage(String stageId) {
        stageExitTimes.putIfAbsent(stageId, new ArrayList<>());
        stageExitTimes.get(stageId).add(System.currentTimeMillis());
    }

    // return the cycle time of the card in the given stage
    public double getCycleTime(String stageId) {
        List<Long> entries = stageEntryTimes.get(stageId);
        List<Long> exits = stageExitTimes.get(stageId);

        // if the card has not entered or exited the stage
        if (entries == null || exits == null || entries.size() != exits.size()) {
            return 0;
        }

        double totalTime = 0;
        for (int i = 0; i < entries.size(); i++) {
            totalTime += (exits.get(i) - entries.get(i));
        }

        // convert milliseconds to days
        return totalTime / (1000.0 * 60 * 60 * 24);
    }

    // 返回阶段的进入和退出时间记录
    public Map<String, List<Long>> getStageEntryTimes() {
        return new HashMap<>(stageEntryTimes); // 返回副本保护数据
    }

    public Map<String, List<Long>> getStageExitTimes() {
        return new HashMap<>(stageExitTimes); // 返回副本保护数据
    }

    @Override
    public String toString() {
        return "Card{" +
                "description='" + description + '\'' +
                ", type=" + type +
                ", boardId='" + boardId + '\'' +
                ", cardId=" + cardId +
                ", stageEntryTimes=" + stageEntryTimes +
                ", stageExitTimes=" + stageExitTimes +
                '}';
    }
}
