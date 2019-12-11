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

    public LightEnemyEntity createLightEnemy(OrthographicCamera camera, Rectangle rectangle) {
        LightEnemyEntity entity =new LightEnemyEntity(rayHandler, camera, world);

        SystemManager.getInstance().addEntity(entity);
        SystemManager.getInstance().addEntityComponent(entity, new LightEnemyPhysics(entity, world, rectangle));
        SystemManager.getInstance().addEntityComponent(entity, new LightEnemyGraphics(entity));

        return entity;
    }

    public LightPlayerEntity createLightPlayer(OrthographicCamera camera) {
        LightPlayerEntity entity =new LightPlayerEntity(rayHandler, camera, world);

        SystemManager.getInstance().addEntity(entity);
        SystemManager.getInstance().addEntityComponent(entity, new LightPlayerInput(entity));
        SystemManager.getInstance().addEntityComponent(entity, new LightPlayerPhysics(entity, world, rayHandler));
        SystemManager.getInstance().addEntityComponent(entity, new LightPlayerGraphics(entity));

        return entity;
    }

    public LightObjectEntity createLightObject(OrthographicCamera camera, Rectangle rectangle, MapProperties mapProperties, boolean isSensor) {
        LightObjectEntity entity=null;

        // @TODO utiliser le layer "enemy_layer" pour éviter ce test
        if (mapProperties.containsKey("type") && mapProperties.get("type").toString().compareTo("ennemy") == 0) {
            createLightEnemy(camera, rectangle);
        }
        else {
            entity= new LightObjectEntity(rayHandler, camera, world);

            SystemManager.getInstance().addEntity(entity);
            SystemManager.getInstance().addEntityComponent(entity, new LightObjectPhysics(entity, world, rayHandler, rectangle, mapProperties, isSensor));
        }

        return entity;
    }

    public LightObjectEntity createLightObject(OrthographicCamera camera, Polygon polygon, MapProperties mapProperties, boolean isSensor) {
        LightObjectEntity entity = new LightObjectEntity(rayHandler, camera, world);

        SystemManager.getInstance().addEntity(entity);
        SystemManager.getInstance().addEntityComponent(entity, new LightObjectPhysics(entity, world, polygon, mapProperties, isSensor));

        return entity;
    }

}
