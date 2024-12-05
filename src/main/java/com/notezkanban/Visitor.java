package com.notezkanban;

import com.notezkanban.lane.Lane;

public interface Visitor {
    void visitLane(Lane lane);
    int getResult();
}
