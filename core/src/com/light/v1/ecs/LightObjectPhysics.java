package com.light.v1.ecs;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.*;
import com.light.v1.element.WorldManager;

public class LightObjectPhysics extends LightPhysics {
    //private static final String TAG = "LightObjectPhysics";

    public LightObjectPhysics(LightObjectEntity entity, World world, RayHandler rayHandler, MapObject object, boolean isSensor, short category, short mask) {
        this.entity=entity;
        this.entity.setMapProperties(object.getProperties());
        fixtureCategory=category;
        fixtureMask=mask;
        createObject(world, rayHandler, object, isSensor);

        this.entity.setPosition(bodyPosition);
        WorldManager.getInstance().add(this.entity);
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