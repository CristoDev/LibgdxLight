package com.light.v1.ECS;

import com.badlogic.gdx.graphics.g2d.Batch;

public class LightEnemyPhysics extends LightPhysics {
    private LightEnemyEntity enemy;

    public LightEnemyPhysics(LightEnemyEntity entity) {
        enemy=entity;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void receiveMessage(ECSEvent.EVENT event, String message) {

    }

    @Override
    public void update(Batch batch) {

    }

    @Override
    public void render(Batch batch) {

    }
}
