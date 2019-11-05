package com.light.v1.ECS;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.light.v1.element.WorldLightTest;
import com.light.v1.element.WorldTorch;

public class LightPlayerEntity extends LightEntity {
    private static final String TAG = "LightPlayerEntity";



    public WorldLightTest elementLight;

    private LightPlayerInput lightPlayerInput;
    private LightPlayerGraphics lightGraphics;

    public LightPlayerEntity(RayHandler _rayHandler, OrthographicCamera _camera, World _world) {
        rayHandler=_rayHandler;
        camera=_camera;
        world=_world;
        lightPlayerInput =new LightPlayerInput();
        lightGraphics=new LightPlayerGraphics();
        lightGraphics.addItem(world, rayHandler);
        lightGraphics.createSword(world);

    }

    public void createLights() {
        elementLight=new WorldLightTest(rayHandler);
    }

    private void addLight() {
        new WorldTorch(rayHandler, camera.position);
    }

    public void update(Batch batch) {
        // @TODO temporaire pour les tests sur les components
        LightPlayerGraphics zz=(LightPlayerGraphics)SystemManager.getInstance().getComponent(this, LightPlayerGraphics.class.getSimpleName());
        Gdx.app.debug(TAG, "on est dans player // "+zz.getData());



        lightPlayerInput.update(this, batch);
        lightGraphics.update(this, batch);

        elementLight.update();
    }

    public Vector2 getPosition() {
        return lightGraphics.getPosition();
    }

    public void sendMessage(ECSEvent.EVENT event, String message) {
        lightPlayerInput.receiveMessage(event, message);
        lightGraphics.receiveMessage(event, message);
    }

    public float getVelocity() {
        return velocity;
    }

    public void elementLightActivate(float screenX, float screenY) {
        Vector3 point=new Vector3(0, 0, 0);
        camera.unproject(point.set(screenX, screenY, 0));
        elementLight.activate(point);
    }

    public void elementLightSetActive(boolean active) {
        elementLight.setActive(active);
    }
}
