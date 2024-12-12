package com.notezkanban.visitor;

import com.notezkanban.lane.Lane;

public interface LaneVisitor<T> {
    void visitLane(Lane lane);
    T getResult();
}
