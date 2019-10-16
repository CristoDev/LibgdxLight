package com.light.v1;

public interface Observer {

    public void onNotify(ObserverNotifier.Event event, String message);

}
