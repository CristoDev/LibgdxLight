package com.light.v1.element;

import com.badlogic.gdx.Gdx;
import com.light.v1.ecs.LightEntity;

import java.util.HashMap;

public class WorldManager {
    private HashMap<Integer, LightEntity> warpDestination= new HashMap<>();
    private static WorldManager worldManager =new WorldManager();

    private WorldManager() {
        // nothing to do...
    }

    public static WorldManager getInstance() {
        return worldManager;
    }

    public void add(LightEntity entity) {
        //MapProperties mapProperties=entity.getMapProperties();

        if (entity.getType() != null) {
               addType(entity);
        }


    }

    public void addType(LightEntity entity) {
        if (entity.getType().compareTo("warp_destination") ==0) {
            addWarpDestination(entity.getPropertyId(), entity);
        }
    }

    public void addWarpDestination(int id, LightEntity entity) {
        warpDestination.put(id, entity);
    }

    public LightEntity getWarp(int id) {
        Gdx.app.debug("WORLD", "recherche de " + id);
        return warpDestination.get(id);
    }
}
