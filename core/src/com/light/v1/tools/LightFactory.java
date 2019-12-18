package com.light.v1.tools;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.light.v1.ecs.*;

public class LightFactory {
    private static World world;
    private static RayHandler rayHandler;
    private static MyMap mapManager = null;
    private OrthographicCamera camera;
    private static LightFactory lightFactory=new LightFactory();

    private LightFactory() {
        // lightFactory
    }

    public void init(World world, RayHandler rayHandler, OrthographicCamera camera) {
        this.camera=camera;
        mapManager = new MyMap();
        LightFactory.world=world;
        LightFactory.rayHandler=rayHandler;
    }

    public static LightFactory getInstance() {
        return lightFactory;
    }

    public LightEnemyEntity createLightEnemy(OrthographicCamera camera, Rectangle rectangle, MapProperties mapProperties) {
        LightEnemyEntity entity =new LightEnemyEntity(world, rayHandler, camera, mapProperties);

        SystemManager.getInstance().addEntity(entity);
        SystemManager.getInstance().addEntityComponent(entity, new LightEnemyPhysics(entity, world, rectangle));
        SystemManager.getInstance().addEntityComponent(entity, new LightEnemyGraphics(entity));

        return entity;
    }

    public LightPlayerEntity createLightPlayer(OrthographicCamera camera) {
        LightPlayerEntity entity =new LightPlayerEntity(world, rayHandler, camera);

        SystemManager.getInstance().addEntity(entity);
        SystemManager.getInstance().addEntityComponent(entity, new LightPlayerInput(entity));
        SystemManager.getInstance().addEntityComponent(entity, new LightPlayerPhysics(entity, world, rayHandler));
        SystemManager.getInstance().addEntityComponent(entity, new LightPlayerGraphics(entity));

        return entity;
    }

    public LightObjectEntity createLightObject(OrthographicCamera camera, MapObject object, boolean isSensor, short category, short mask) {
        LightObjectEntity entity= new LightObjectEntity(world, rayHandler, camera, object.getProperties());

        SystemManager.getInstance().addEntity(entity);
        SystemManager.getInstance().addEntityComponent(entity, new LightObjectPhysics(entity, world, rayHandler, object, isSensor, category, mask));

        return entity;
    }

    public void buildMap() {
        buildLayer(mapManager.getWallLayer(), false, ECSFilter.WALL, ECSFilter.MASK_WALL);
        buildLayer(mapManager.getObstacleLayer(), false, ECSFilter.OBSTACLE, ECSFilter.MASK_OBSTACLE);
        buildEnnemyLayer(mapManager.getEnemyLayer());
        buildLayer(mapManager.getFloorLayer(), true, ECSFilter.FLOOR, ECSFilter.MASK_FLOOR);
        buildLayer(mapManager.getInteractionLayer(), true, ECSFilter.INTERACTION, ECSFilter.MASK_INTERACTION);
    }

    private void buildEnnemyLayer(MapLayer layer) {
        if (layer == null) {
            return ;
        }

        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                createLightEnemy(camera, ((RectangleMapObject) object).getRectangle(), object.getProperties());
            }
        }
    }

    private boolean buildLayer(MapLayer layer, boolean isSensor, short category, short mask) {
        if (layer == null) {
            return false;
        }

        for (MapObject object : layer.getObjects()) {
            createLightObject(camera, object, isSensor, category, mask);
        }

        return true;
    }

    public static MyMap getMap() {
        return mapManager;
    }

    public int[] getBackLayers() {
        return mapManager.getBackLayers();
    }

    public int[] getFrontLayers() {
        return mapManager.getFrontLayers();
    }

}
