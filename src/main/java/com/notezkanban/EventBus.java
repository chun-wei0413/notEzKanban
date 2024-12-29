package com.notezkanban;

import com.notezkanban.notifier.Notifier;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private static final EventBus INSTANCE = null;
    private final Map<String, List<Notifier>> boardTeams = new ConcurrentHashMap<>();

    private EventBus() {}

    public static EventBus getInstance() {
        if (INSTANCE == null) {
            return new EventBus();
        }
        return INSTANCE;
    }

    // 每個board都有1到多個notifier
    public void registerNotifier(String boardId, Notifier notifier) {
        boardTeams.computeIfAbsent(boardId, k -> new CopyOnWriteArrayList<>()).add(notifier);
    }

    // 取消註冊 Notifier
    public void unregisterNotifier(String boardId, Notifier notifier) {
        List<Notifier> notifiers = boardTeams.get(boardId);
        if (notifiers != null) {
            notifiers.remove(notifier);
            if (notifiers.isEmpty()) {
                boardTeams.remove(boardId); // 如果列表為空，移除 key
            }
        }
    }
    // 發佈事件，通知對應的團隊
    public void publish(Event event) {
        List<Notifier> notifiers = boardTeams.get(event.getBoardId());
        if (notifiers != null) {
            for (Notifier notifier : notifiers) {
                notifier.notify(event.getMessage());
            }
        } else {
            System.err.println("No team registered for boardId: " + event.getBoardId());
        }
    }
}
