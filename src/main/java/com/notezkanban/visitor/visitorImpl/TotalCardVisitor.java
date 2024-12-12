package com.notezkanban.visitor.visitorImpl;

import java.util.Iterator;

import com.notezkanban.lane.Lane;
import com.notezkanban.visitor.LaneVisitor;

public class TotalCardVisitor implements LaneVisitor {
    int totalCardCount = 0;

    @Override
    public void visitLane(Lane lane) {
        totalCardCount += lane.getCards().size();
        Iterator<Lane> it = lane.iterator(); 
        while (it.hasNext()) {
            it.next().accept(this);
        }
    }



    @Override
    public Integer getResult() {
        return totalCardCount;
    }
    
}
