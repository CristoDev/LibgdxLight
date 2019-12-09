package com.light.v1.ecs;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;
import java.util.HashMap;

public class SystemManager {
    private static final String TAG = "SystemManager";
    private ArrayList<LightEntity> entities=new ArrayList<LightEntity>();
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
        entities.add(entity);
        addEntityComponents(entity, new ArrayList<Component>());
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

    public void update(Batch batch) {
        for (LightEntity entity : entities) {
            update(entity, batch);
        }
    }

    public void update(LightEntity entity, Batch batch) {
        ArrayList<Component> components=getEntityComponents(entity);

        for (Component component : components) {
            component.update(batch);
        }

        entity.update(batch);
    }

    public void render(Batch batch) {
        for (LightEntity entity : entities) {
            render(entity, batch);
        }
    }

    public void render(LightEntity entity, Batch batch) {
        ArrayList<Component> components=getEntityComponents(entity);

        for (Component component : components) {
            component.render(batch);
        }

        entity.render(batch);

    }

    public void codeTest() {
        /*
        systemManager.addEntity(lightPlayerEntity);
        systemManager.addEntityComponent(lightPlayerEntity, new LightPlayerInput());
        systemManager.addEntityComponent(lightPlayerEntity, new LightPlayerGraphics());

        ArrayList<Component> elements=systemManager.getEntityComponents(lightPlayerEntity);
        for (int i=0; i<elements.size(); i++) {
            String name=elements.get(i).getClass().getSimpleName();
            Gdx.app.debug(TAG, "element "+name);
            if (name.compareTo(LightPlayerGraphics.class.getSimpleName()) == 0) {
                Gdx.app.debug(TAG, "element graphic trouvé");
                Gdx.app.debug(TAG, ((LightPlayerGraphics)elements.get(i)).getData());
            }
        }

        // nécessite un test  == null
        LightPlayerGraphics x =(LightPlayerGraphics)systemManager.getComponent(lightPlayerEntity, LightPlayerGraphics.class.getSimpleName());
        Gdx.app.debug(TAG, "test sur x "+x.getData());
         */
    }
}
