package com.light.v1;

import java.util.ArrayList;

public class ObserverNotifier {
    public static enum Event {
        MOVE_LEFT,
        MOVE_RIGHT,
        MOVE_UP,
        MOVE_DOWN,
        IDLE
    }

    private static ArrayList<Observer> observers=new ArrayList<Observer>();

    public ObserverNotifier() {

    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    protected void notify(ObserverNotifier.Event event, String message){
        for(Observer observer : observers){
            observer.onNotify(event, message);
        }
    }
}
