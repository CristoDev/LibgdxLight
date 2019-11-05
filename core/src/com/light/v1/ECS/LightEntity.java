package com.light.v1.ECS;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class LightEntity {
    protected RayHandler rayHandler;
    protected OrthographicCamera camera;
    protected World world;

    protected float velocity=3f;
    protected Vector2 position;

    public LightEntity() {

    }


}
