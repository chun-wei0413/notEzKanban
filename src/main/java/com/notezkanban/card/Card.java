package com.notezkanban.card;

import com.notezkanban.card.exception.CardException;

import java.util.UUID;

public class Card {
    private String description;
    private CardType type;
    private String boardId;
    private UUID cardId;

    public Card(String description, CardType type, String boardId) {
        if (isCardInvalid(description)) {
            throw new CardException("Invalid card create");
        }
        this.description = description;
        this.type = type;
        this.boardId = boardId;
        this.cardId = UUID.randomUUID();
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
}
