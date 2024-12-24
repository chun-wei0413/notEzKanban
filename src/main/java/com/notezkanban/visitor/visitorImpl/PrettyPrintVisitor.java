package com.notezkanban.visitor.visitorImpl;

import com.notezkanban.card.Card;
import com.notezkanban.iterator.DFSLaneIterator;
import com.notezkanban.lane.Lane;
import com.notezkanban.lane.Stage;
import com.notezkanban.lane.SwimLane;
import com.notezkanban.visitor.LaneVisitor;

public class PrettyPrintVisitor implements LaneVisitor {
    private StringBuilder result;
    private static final String INDENT = "  ";

    public PrettyPrintVisitor() {
        result = new StringBuilder();
    }

    @Override
    public void visitStage(Stage stage) {
        DFSLaneIterator iterator = new DFSLaneIterator(stage);
        while (iterator.hasNext()) {
            Lane currentLane = iterator.next();
            printLaneWithIndent(currentLane, iterator.getDepth());
        }
    }

    @Override
    public void visitSwimLane(SwimLane swimLane) {
        DFSLaneIterator iterator = new DFSLaneIterator(swimLane);
        while (iterator.hasNext()) {
            Lane currentLane = iterator.next();
            printLaneWithIndent(currentLane, iterator.getDepth());
        }
    }

    private void printLaneWithIndent(Lane lane, int currentDepth) {
        // 打印縮進
        for (int i = 0; i < currentDepth; i++) {
            result.append(INDENT);
        }
        
        // 打印當前 Lane
        result.append(lane.getClass().getSimpleName())
             .append(": ")
             .append(lane.getLaneName())
             .append("\n");

        // 打印卡片
        if (!lane.getCards().isEmpty()) {
            for (Card card : lane.getCards()) {
                for (int i = 0; i < currentDepth + 1; i++) {
                    result.append(INDENT);
                }
                result.append(" Card: ")
                     .append(card.getDescription())
                     .append(" (")
                     .append(card.getType())
                     .append(")\n");
            }
        }
    }

    @Override
    public String getResult() {
        return result.toString();
    }
}
