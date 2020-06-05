package com.light.v1.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.light.v1.tools.MyMap;

public class LightEnemyGraphics extends LightGraphics {
    private static final String TAG = "LightEnemyGraphics";

    public LightEnemyGraphics(LightEnemyEntity entity) {
        this.entity=entity;
        createEnemy();
    }

    @Override
    public void update(float delta) {
        animationEntity.update(delta);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        animationEntity.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "dispose");
    }

    @Override
    public void receiveMessage(ECSEvent.Event event, String message) {
        String[] string=message.split(ECSEvent.MESSAGE_TOKEN);

        if (event == ECSEvent.Event.SET_POSITION) {
            animationEntity.setPosition(Float.parseFloat(string[0])- itemWidth * MyMap.UNIT_SCALE / 2, Float.parseFloat(string[1])- itemWidth * MyMap.UNIT_SCALE / 2);
        }
        else if (event == ECSEvent.Event.SET_STATE) {
            animationEntity.setAnimationState(message);
        }
        else if (event == ECSEvent.Event.SET_DIRECTION) {
            animationEntity.setAnimationDirection(message);
        }
    }
}
