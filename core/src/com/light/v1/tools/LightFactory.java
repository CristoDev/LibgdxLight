package com.light.v1.tools;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.light.v1.ecs.*;

public class LightFactory {
    private static World world;
    private static RayHandler rayHandler;
    private static LightFactory lightFactory=new LightFactory();

    private LightFactory() {
        // lightFactory
    }

    public void init(World world, RayHandler rayHandler) {
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

    public LightObjectEntity createLightObject(OrthographicCamera camera, Rectangle rectangle, MapProperties mapProperties, boolean isSensor) {
        LightObjectEntity entity= new LightObjectEntity(world, rayHandler, camera, mapProperties);

        SystemManager.getInstance().addEntity(entity);
        SystemManager.getInstance().addEntityComponent(entity, new LightObjectPhysics(entity, world, rayHandler, rectangle, mapProperties, isSensor));

        return entity;
    }

    public LightObjectEntity createLightObject(OrthographicCamera camera, Polygon polygon, MapProperties mapProperties, boolean isSensor) {
        LightObjectEntity entity= new LightObjectEntity(world, rayHandler, camera, mapProperties);

        SystemManager.getInstance().addEntity(entity);
        SystemManager.getInstance().addEntityComponent(entity, new LightObjectPhysics(entity, world, polygon, mapProperties, isSensor));

        return entity;
    }

}
