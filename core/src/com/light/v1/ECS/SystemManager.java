package com.light.v1.ECS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class SystemManager {
    private static final String TAG = "SystemManager";
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

    public ArrayList<Component> getComponents(LightEntity entity) {
        return ecsData.get(entity);
    }

    public Component getComponent(LightEntity entity, String className) {
        ArrayList<Component> components=getEntityComponents(entity);

        for (Component component : components) {
            if (component.getClass().getSimpleName().compareTo(className) == 0) {
                return component;
            }
        }

        return null;
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

    public void sendMessage(LightEntity entity, ECSEvent.EVENT event, String message) {
        ArrayList<Component> components=getEntityComponents(entity);

        for (Component component : components) {
            component.receiveMessage(event, message);
        }
    }

    public void update(LightEntity entity, Batch batch) {
        ArrayList<Component> components=getEntityComponents(entity);

        for (Component component : components) {
            component.update(entity, batch);
        }
    }
}