package com.light.v1.ecs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.light.v1.element.WorldManager;

public class LightObjectPhysics extends LightPhysics {
    //private static final String TAG = "LightObjectPhysics";

    public LightObjectPhysics(LightObjectEntity entity, MapObject object, boolean isSensor, short category, short mask) {
        this.entity=entity;
        this.entity.setMapProperties(object.getProperties());
        fixtureCategory=category;
        fixtureMask=mask;
        createObject(object, isSensor);

        //this.entity.setPosition(bodyPosition);
        SystemManager.getInstance().sendMessage(this.entity, ECSEvent.Event.SET_POSITION, bodyPosition.x+ECSEvent.MESSAGE_TOKEN+bodyPosition.y);
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
    public void update(float delta) {
        // update
    }

    @Override
    public void render(SpriteBatch batch) {
        // render
    }
}