package com.notezkanban.card;

public class CardFactory {
    public static Card createCard(String description, CardType type) {
        return new Card(description, type);
    }
}
