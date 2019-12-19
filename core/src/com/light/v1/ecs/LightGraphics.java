package com.light.v1.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.light.v1.tools.MyMap;

public abstract class LightGraphics implements Component {
    protected LightEntity entity;
    protected Sprite sprite;
    protected Vector2 position=new Vector2(0, 0);
    protected float itemWidth=16;

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
}
