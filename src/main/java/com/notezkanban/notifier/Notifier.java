package com.notezkanban.notifier;

import com.notezkanban.boardMember.BoardMember;

public interface Notifier {
    void notify(String message);
    String message();
}
