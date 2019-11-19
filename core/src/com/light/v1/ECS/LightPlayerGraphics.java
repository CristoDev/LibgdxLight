package com.light.v1.ECS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

public class LightPlayerGraphics extends LightGraphics {
    private static final String TAG = "LightPlayerGraphics";

    /*
    utilisation de la classe
    - affichage du sprite en fonction de la direction et de la position
    - affichage des animations --> utilisation d'un assetManager
     */

    public LightPlayerGraphics(LightPlayerEntity entity) {
        this.entity=entity;
        init();
    }

    public void init() {
        entity.createSprite("item.png");
    }

    @Override
    public void update(Batch batch) {

    }

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
    }

}
