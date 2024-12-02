package com.notezkanban;

import com.notezkanban.exception.CardException;

import java.util.UUID;

public class Card {
    private String description;
    private CardType type;
    private UUID cardId;

    public Card(String description, CardType type) {
        if (isCardInvalid(description)) {
            throw new CardException("Invalid card create");
        }
        this.description = description;
        this.type = type;
        this.cardId = UUID.randomUUID();
    }

    private boolean isCardInvalid(String description) {
        return description.trim().isEmpty();
    }

    public CardType getType() {
        return type;
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
