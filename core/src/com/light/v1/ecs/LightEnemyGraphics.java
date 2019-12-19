package com.light.v1.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.light.v1.tools.MyMap;

public class LightEnemyGraphics extends LightGraphics {
    private static final String TAG = "LightEnemyGraphics";

    public LightEnemyGraphics(LightEnemyEntity entity) {
        this.entity=entity;
        createSprite("enemy.png");
    }

    @Override
    public void update(Batch batch) {
        // update
    }

    @Override
    public void render(Batch batch) {
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "dispose");
    }

    @Override
    public void receiveMessage(ECSEvent.Event event, String message) {
        if (event == ECSEvent.Event.SET_POSITION) {
            String[] string=message.split(ECSEvent.MESSAGE_TOKEN);
            sprite.setPosition(Float.parseFloat(string[0])- itemWidth * MyMap.UNIT_SCALE / 2, Float.parseFloat(string[1])- itemWidth * MyMap.UNIT_SCALE / 2);
        }

    }
}
