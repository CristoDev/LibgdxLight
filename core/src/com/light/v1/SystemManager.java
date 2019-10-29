package com.light.v1;

import java.util.ArrayList;

public class SystemManager {
    private LightPlayer player;
    private ArrayList<Component> components;

    public SystemManager() {
        components=new ArrayList<Component>();
    }

    public void initPlayer(LightPlayer _player) {
        player=_player;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void removeComponent(Component component) {
        components.remove(component);
    }

    public void sendMessage(String event, String message) {
        for (Component component : components) {
            component.receiveMessage(event, message);
        }
    }

}
