package com.notezkanban;

public interface Command {
    void execute();
    void undo();
}
