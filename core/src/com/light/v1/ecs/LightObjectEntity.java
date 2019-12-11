package com.light.v1.ecs;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;

public class LightObjectEntity extends LightEntity {

    public LightObjectEntity(RayHandler _rayHandler, OrthographicCamera _camera, World _world) {
        super(_rayHandler, _camera, _world);
    }

}
