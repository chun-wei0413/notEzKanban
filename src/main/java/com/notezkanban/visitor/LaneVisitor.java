package com.notezkanban.visitor;

import com.notezkanban.lane.Stage;
import com.notezkanban.lane.SwimLane;

public interface LaneVisitor<T> {
    void visitStage(Stage stage);
    void visitSwimLane(SwimLane swimLane);
    T getResult();
}
