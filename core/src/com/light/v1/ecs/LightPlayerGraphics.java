package com.light.v1.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
        createPlayer();
        SystemManager.getInstance().sendMessage(entity, ECSEvent.Event.INIT_COMPONENT, Float.toString(itemWidth));
    }

    @Override
    public void update(float delta) {
        animationEntity.update(delta);
        entity.getCamera().position.set(animationEntity.getPosition().x, animationEntity.getPosition().y, 0);
    }

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
            animationEntity.setPosition(Float.parseFloat(string[0])- itemWidth * MyMap.UNIT_SCALE, Float.parseFloat(string[1])- itemWidth * MyMap.UNIT_SCALE / 2);
        }
        else if (event == ECSEvent.Event.SET_STATE) {
            animationEntity.setAnimationState(message);
        }
        else if (event == ECSEvent.Event.SET_DIRECTION) {
            animationEntity.setAnimationDirection(message);
        }
    }

}
