package com.notezkanban.notifier.notifierImpl;

import com.notezkanban.notifier.Notifier;

public class LineNotifier implements Notifier {
    private String message;

    @Override
    public void notify(String message) {
        this.message = message;
        System.out.println("LineNotifier: " + message);
    }

    @Override
    public String message(){
        return message;
    }
}
