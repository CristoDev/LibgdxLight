package com.light.v1;

import java.util.ArrayList;
import java.util.HashMap;

public class SystemManager {
    private HashMap<String, ArrayList<Component>> ecsData=new HashMap<String, ArrayList<Component>>();
    private static SystemManager systemManager=new SystemManager();

    private SystemManager() {
    }

    public static SystemManager getInstance() {
        return systemManager;
    }

    public ArrayList<Component> getEntityComponents(LightEntity entity) {
        return ecsData.get(entity.toString());
    }

    public void addEntity(LightEntity entity) {
        addEntityComponents(entity, new ArrayList<Component>());
    }

    public void addEntityComponents(LightEntity entity, ArrayList<Component> components) {
        ecsData.put(entity.toString(), components);
    }


    public void addEntityComponent(LightEntity entity, Component component) {
        ArrayList<Component> components=getEntityComponents(entity);
        components.add(component);

        addEntityComponents(entity, components);
    }

    public void removeEntityComponent(LightEntity entity, Component component) {
        ArrayList<Component> components=getEntityComponents(entity);
        components.remove(component);

        addEntityComponents(entity, components);
    }

    public void sendMessage(LightEntity entity, String event, String message) {
        ArrayList<Component> components=getEntityComponents(entity);

        for (Component component : components) {
            component.receiveMessage(event, message);
        }
    }

}
