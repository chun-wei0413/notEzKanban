package com.notezkanban;

public class MoveCardCommand implements Command {
    private Lane source;
    private Lane destination;
    private Card card;

    public MoveCardCommand(Lane source, Lane destination, Card card) {
        this.source = source;
        this.destination = destination;
        this.card = card;
    }

    @Override
    public void execute() {
        if(source.getCard(card.getId()).isEmpty()) {
            throw new IllegalArgumentException("Card not found in source lane");
        }
        source.deleteCard(card);
        destination.addCard(card);
    }

    @Override
    public void undo() {
        destination.deleteCard(card);
        source.addCard(card);
    }
}
