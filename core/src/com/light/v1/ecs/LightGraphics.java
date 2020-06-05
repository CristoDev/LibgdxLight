package com.light.v1.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.light.v1.tools.AnimationEntity;
import com.light.v1.tools.MyMap;

public abstract class LightGraphics implements Component {
    protected LightEntity entity;
    protected Sprite sprite;
    protected Vector2 position=new Vector2(0, 0);
    protected float itemWidth=16;
    protected AnimationEntity animationEntity=new AnimationEntity();

    public Vector2 getPosition() {
        return position;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getItemWidth() {
        return itemWidth;
    }


    public void createSprite(String filename) {
        Texture texture = new Texture(Gdx.files.internal(filename));
        sprite = new Sprite(texture);
        sprite.setSize(sprite.getWidth() * MyMap.UNIT_SCALE, sprite.getHeight() * MyMap.UNIT_SCALE);
        sprite.setPosition(position.x - itemWidth * MyMap.UNIT_SCALE / 2, position.y - itemWidth * MyMap.UNIT_SCALE / 2);
        sprite.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        sprite.setOriginCenter();
    }

    public void createPlayer() {
        animationEntity=new AnimationEntity();

        animationEntity.createHuman();
        animationEntity.init();
        //animationEntity.setPosition(200, 50);
        animationEntity.setPosition(position.x - itemWidth * MyMap.UNIT_SCALE, position.y - itemWidth * MyMap.UNIT_SCALE / 2);
        animationEntity.addEquipment("torso/chain/mail_male.png");
        //animationEntity.addEquipment("hands/gloves/male/metal_gloves_male.png");
        animationEntity.addEquipment("weapons/right hand/male/dagger_male.png");
        animationEntity.setAnimationDirection(ECSEvent.AnimationDirection.RIGHT);
        animationEntity.setAnimationState(ECSEvent.AnimationState.IDLE);
        animationEntity.loadAllAnimations();
    }

    public void createEnemy() {
        animationEntity=new AnimationEntity();

        animationEntity.createOrc();
        animationEntity.init();
        //animationEntity.addEquipment("torso/chain/mail_male.png");
        //animationEntity.addEquipment("hands/gloves/male/metal_gloves_male.png");
        //animationEntity.addEquipment("weapons/right hand/male/dagger_male.png");
        animationEntity.setAnimationDirection(ECSEvent.AnimationDirection.RIGHT);
        animationEntity.setAnimationState(ECSEvent.AnimationState.WALK);
        animationEntity.loadAllAnimations();

    }

}
