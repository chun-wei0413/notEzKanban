package com.notezkanban.iterator;

import com.notezkanban.lane.Lane;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class NullIterator implements Iterator<Lane> {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Lane next() {
        throw new NoSuchElementException("Null iterator does not point to any element.");
    }
}
