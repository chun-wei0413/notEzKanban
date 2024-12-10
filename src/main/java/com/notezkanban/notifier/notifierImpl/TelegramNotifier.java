package com.notezkanban.notifier.notifierImpl;

import com.notezkanban.notifier.Notifier;

public class TelegramNotifier implements Notifier {
    private String message;

    @Override
    public void notify(String message) {
        this.message = message;
        System.out.println("TelegramNotifier: " + message);
    }

    @Override
    public String message(){
        return message;
    }
}
