package com.notezkanban;

import com.notezkanban.exception.CardException;

public class Card {
    private String description;
    private CardType type;

    public Card(String description, CardType type) {
        if (isCardInvalid(description)) {
            throw new CardException("Invalid card create");
        }
        this.description = description;
        this.type = type;
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
}
