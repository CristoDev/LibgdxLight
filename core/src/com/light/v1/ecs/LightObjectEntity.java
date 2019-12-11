package com.light.v1.ecs;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.World;

public class LightObjectEntity extends LightEntity {
    public LightObjectEntity(World world, RayHandler rayHandler, OrthographicCamera camera, MapProperties mapProperties) {
        super(world, rayHandler, camera, mapProperties);
    }
}
