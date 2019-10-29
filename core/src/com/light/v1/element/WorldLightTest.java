package com.light.v1.element;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class WorldLightTest implements WorldElement {
    private static final String TAG = "WorldLightTest";
    protected Light element, loop;

    public WorldLightTest(RayHandler rayHandler) {
        element = new PointLight(rayHandler, 32);
        element.setActive(false);
        element.setColor(Color.PURPLE);
        element.setDistance(5f);

        /*
        loop = new PointLight(rayHandler, 16, Color.YELLOW, 1f, 5, 5);
        loop.setActive(true);

        Light conelight = new ConeLight(rayHandler, 32, Color.WHITE, 20, 5, 5, 270, 45);
        conelight.setSoft(false);
         */
    }

    @Override
    public void update() {
        //float elapsedTime = TimeUtils.timeSinceMillis(startTime) / 1000f;
        //loop.setPosition(5 + 3 * MathUtils.cos(elapsedTime), 8 + 2 * MathUtils.sin(elapsedTime));
    }

    public void activate(Vector3 point) {
        element.setPosition(point.x, point.y);
        element.setActive(true);

    }

    public void setActive(boolean active) {
        element.setActive(active);
    }

    public void setPosition(Vector3 point) {
        element.setPosition(point.x, point.y);
    }
}
