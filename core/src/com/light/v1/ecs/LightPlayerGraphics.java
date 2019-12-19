package com.light.v1.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.light.v1.tools.MyMap;

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
        createSprite("item.png");
        SystemManager.getInstance().sendMessage(entity, ECSEvent.Event.INIT_COMPONENT, Float.toString(itemWidth));
    }



    @Override
    public void update(Batch batch) {
        // update
        entity.getCamera().position.set(sprite.getX(), sprite.getY(), 0);
    }

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
