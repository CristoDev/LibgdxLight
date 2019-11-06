package com.light.v1.ECS;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class LightEntity {
    private static final String TAG = "LightEntity";

    protected RayHandler rayHandler;
    protected OrthographicCamera camera;
    protected World world;

    protected float velocity=3f;

    LightEntity(RayHandler _rayHandler, OrthographicCamera _camera, World _world) {
        rayHandler=_rayHandler;
        camera=_camera;
        world=_world;
    }

    public Vector2 getPosition() {
        LightPlayerGraphics graphics =(LightPlayerGraphics)SystemManager.getInstance().getComponent(this, LightPlayerGraphics.class.getSimpleName());

        if (graphics == null) {
            Gdx.app.error(TAG, "Component LightPlayerGraphics not found -> position to (0,0)");
            return new Vector2(0, 0);
        }

        return graphics.getPosition();
    }

    public float getVelocity() {
        return velocity;
    }

    public void update(Batch batch) {
    }
}
