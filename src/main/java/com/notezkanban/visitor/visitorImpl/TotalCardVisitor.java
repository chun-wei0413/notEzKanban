package com.notezkanban.visitor.visitorImpl;

import java.util.Iterator;

import com.notezkanban.lane.Lane;
import com.notezkanban.lane.Stage;
import com.notezkanban.lane.SwimLane;
import com.notezkanban.visitor.LaneVisitor;

public class TotalCardVisitor implements LaneVisitor {
    int totalCardCount = 0;

    @Override
    public void visitStage(Stage stage) {
        totalCardCount += stage.getCards().size();
        Iterator<Lane> it = stage.iterator();
        while (it.hasNext()) {
            it.next().accept(this);
        }
    }

    @Override
    public void visitSwimLane(SwimLane swimLane) {
        totalCardCount += swimLane.getCards().size();
        Iterator<Lane> it = swimLane.iterator();
        while (it.hasNext()) {
            it.next().accept(this);
        }
    }

    @Override
    public Integer getResult() {
        return totalCardCount;
    }
    
}
