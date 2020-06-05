package com.light.v1.ecs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.HashMap;

public class SystemManager {
    //private static final String TAG = "SystemManager";
    private ArrayList<LightEntity> entities= new ArrayList<>();
    private HashMap<String, ArrayList<Component>> ecsData= new HashMap<>();
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
        entities.add(entity);
        addEntityComponents(entity, new ArrayList<>());
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

    public void sendMessage(LightEntity entity, ECSEvent.Event event, String message) {
        ArrayList<Component> components=getEntityComponents(entity);

        for (Component component : components) {
            component.receiveMessage(event, message);
        }
    }

    /*
    public void update(Batch batch) {
        for (LightEntity entity : entities) {
            update(entity, batch);
        }
    }
     */

    public void update(float delta) {
        for (LightEntity entity : entities) {
            update(entity, delta);
        }
    }

/*
    public void update(LightEntity entity, Batch batch) {
        ArrayList<Component> components=getEntityComponents(entity);

        for (Component component : components) {
            component.update(batch);
        }

        entity.update(batch);
    }

 */

    public void update(LightEntity entity, float delta) {
        ArrayList<Component> components=getEntityComponents(entity);

        for (Component component : components) {
            component.update(delta);
        }

        entity.update(delta);
    }


    public void render(SpriteBatch batch) {
        for (LightEntity entity : entities) {
            render(entity, batch);
        }
    }

    public void render(LightEntity entity, SpriteBatch batch) {
        ArrayList<Component> components=getEntityComponents(entity);

        for (Component component : components) {
            component.render(batch);
        }

        entity.render(batch);

    }

}
