package com.light.v1.ecs;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.light.v1.element.WorldLightTest;

public class LightPlayerEntity extends LightEntity {
    //private static final String TAG = "LightPlayerEntity";
    private WorldLightTest elementLight;

    public LightPlayerEntity(RayHandler _rayHandler, OrthographicCamera _camera, World _world) {
        super(_rayHandler, _camera, _world);
    }

    public void createLights() {
        elementLight=new WorldLightTest(rayHandler);
    }

    public void update(Batch batch) {
        elementLight.update();
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