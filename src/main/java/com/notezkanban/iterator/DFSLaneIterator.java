package com.notezkanban.iterator;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

import com.notezkanban.lane.Lane;

public class DFSLaneIterator implements Iterator<Lane> {
    private Stack<Pair<Lane, Integer>> stack;
    private int currentDepth;

    public DFSLaneIterator(Lane root) {
        stack = new Stack<>();
        stack.push(new Pair<>(root, 0));
        currentDepth = 0;
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public Lane next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        Pair<Lane, Integer> current = stack.pop();
        Lane currentLane = current.getFirst();
        currentDepth = current.getSecond();

        // 將子節點按相反順序壓入堆疊（這樣可以保持正確的遍歷順序）
        List<Lane> children = currentLane.getChildren();
        for (int i = children.size() - 1; i >= 0; i--) {
            stack.push(new Pair<>(children.get(i), currentDepth + 1));
        }

        return currentLane;
    }

    public int getDepth() {
        return currentDepth;
    }
}
