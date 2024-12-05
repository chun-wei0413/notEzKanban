package com.notezkanban;

import com.notezkanban.lane.Lane;

import java.util.Iterator;

public class WIPCountVisitor implements Visitor{
    private int totalCards;

    @Override
    public void visitLane(Lane lane) {
        totalCards += lane.getCards().size();

        Iterator<Lane> it = lane.iterator();
        while(it.hasNext()) {
            it.next().accept(this);
        }
    }

    @Override
    public int getResult() {
        return totalCards;
    }
}
