package com.light.v1.element;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class WorldTorch implements WorldElement {
    //private static final String TAG = "WorldElementLight";
    protected Light element;

    public WorldTorch(RayHandler rayHandler, Vector3 point) {
        element = new PointLight(rayHandler, 16);
        element.setColor(new Color(0.1f, 0.1f, 0.1f, 0.5f));
        element.setDistance(8f);
        element.setActive(true);
        element.setPosition(point.x, point.y);
    }

    @Override
    public void update(float delta) {
        // update
    }
}
