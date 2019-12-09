package com.light.v1.ECS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

public class LightEnemyGraphics extends LightGraphics {
    private static final String TAG = "LightEnemyGraphics";

    public LightEnemyGraphics(LightEnemyEntity entity) {
        this.entity=entity;
        entity.createSprite("enemy.png");
    }

    @Override
    public void update(Batch batch) {
        // update
    }

    @Override
    public void render(Batch batch) {
        batch.begin();
        entity.getSprite().draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "dispose");
    }

    @Override
    public void receiveMessage(ECSEvent.EVENT event, String message) {
        // receive message
    }
}
