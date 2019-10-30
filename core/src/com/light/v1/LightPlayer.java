package com.light.v1;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.light.v1.element.WorldLightTest;
import com.light.v1.element.WorldTorch;

public class LightPlayer extends LightEntity {
    private static final String TAG = "LightPlayer";

    private float velocity=3f;

    private WorldLightTest elementLight;

    private LightPlayerInput lightPlayerInput;
    private LightPlayerGraphics lightGraphics;

    public LightPlayer(RayHandler _rayHandler, OrthographicCamera _camera, World _world) {
        rayHandler=_rayHandler;
        camera=_camera;
        world=_world;
        lightPlayerInput =new LightPlayerInput();
        lightGraphics=new LightPlayerGraphics();
        lightGraphics.addItem(world, rayHandler);
        lightGraphics.createSword(world);

        Gdx.app.debug(TAG, "ID light player: "+this.toString());

    }

    public void createLights() {
        elementLight=new WorldLightTest(rayHandler);
    }

    private void addLight() {
        new WorldTorch(rayHandler, camera.position);
    }

    public void update(Batch batch) {
        lightPlayerInput.update(this, 0, batch);
        lightGraphics.update(this, 0, batch);

        elementLight.update();
    }

    public Vector2 getPosition() {
        return lightGraphics.getPosition();
    }

    public void sendMessage(SystemManager.MESSAGE event, String message) {
        lightPlayerInput.receiveMessage(event, message);
        lightGraphics.receiveMessage(event, message);
    }

    public float getVelocity() {
        return velocity;
    }
}
