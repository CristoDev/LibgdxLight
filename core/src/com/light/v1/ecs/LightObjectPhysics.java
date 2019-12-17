package com.light.v1.ecs;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.*;

public class LightObjectPhysics extends LightPhysics {
    //private static final String TAG = "LightObjectPhysics";

    public LightObjectPhysics(LightObjectEntity entity, World world, RayHandler rayHandler, MapObject object, boolean isSensor, short category, short mask) {
        this.entity=entity;
        this.entity.setType(object.getProperties());
        fixtureCategory=category;
        fixtureMask=mask;

        createObject(world, rayHandler, object, isSensor);
    }

    @Override
    public void dispose() {
        // dispose
    }

    @Override
    public void receiveMessage(ECSEvent.Event event, String message) {
        // receiveMessage
    }

    @Override
    public void update(Batch batch) {
        // update
    }

    @Override
    public void render(Batch batch) {
        // render
    }
}
