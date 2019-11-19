package com.light.v1.ECS;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.light.v1.tools.MyMap;

public class LightPlayerGraphics extends LightGraphics {
    private static final String TAG = "LightPlayerGraphics";

    private Sprite item = null;
    private float itemWidth=16;

    private LightPlayerPhysics physics;

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

        physics=((LightPlayerPhysics)systemManager.getComponent(entity, LightPlayerPhysics.class.getSimpleName()));

        Texture texture = new Texture(Gdx.files.internal("item.png"));
        // @TODO creer une classe pour ajouter un nom au sprite et l'utiliser dans bodyItem.setUserData
        item = new Sprite(texture);
        item.setSize(item.getWidth() * MyMap.UNIT_SCALE, item.getHeight() * MyMap.UNIT_SCALE);
        item.setPosition(physics.getPosition().x - itemWidth * MyMap.UNIT_SCALE / 2, physics.getPosition().y - itemWidth * MyMap.UNIT_SCALE / 2);
        item.setBounds(item.getX(), item.getY(), item.getWidth(), item.getHeight());

        physics.setUserData(item);
    }


    @Override
    public void update(Batch batch) {
        item.setPosition(physics.getPosition().x - 16 * MyMap.UNIT_SCALE / 2, physics.getPosition().y - 16 * MyMap.UNIT_SCALE / 2);
    }

    public void render(Batch batch) {
        batch.begin();
        physics.getUserData().draw(batch);
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
