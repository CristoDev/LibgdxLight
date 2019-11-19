package com.light.v1.ECS;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.light.v1.tools.MyMap;

public class LightEntity {
    private static final String TAG = "LightEntity";

    protected RayHandler rayHandler;
    protected OrthographicCamera camera;
    protected World world;

    protected float velocity=3f;

    protected Vector2 position=new Vector2(0, 0);
    protected Sprite sprite;
    private float itemWidth=16;

    LightEntity(RayHandler _rayHandler, OrthographicCamera _camera, World _world) {
        rayHandler=_rayHandler;
        camera=_camera;
        world=_world;
    }

    public void setPosition(Vector2 _position) {
        position.x=_position.x - itemWidth * MyMap.UNIT_SCALE / 2;
        position.y=_position.y - itemWidth * MyMap.UNIT_SCALE / 2;

        sprite.setPosition(position.x, position.y);
    }


    public Vector2 getPosition() {
        return position;
    }

    public float getVelocity() {
        return velocity;
    }

    public void update(Batch batch) {

    }

    public void render(Batch batch) {

    }

    public void createSprite(String filename) {
        Texture texture = new Texture(Gdx.files.internal(filename));
        sprite = new Sprite(texture);
        sprite.setSize(sprite.getWidth() * MyMap.UNIT_SCALE, sprite.getHeight() * MyMap.UNIT_SCALE);
        sprite.setPosition(position.x - itemWidth * MyMap.UNIT_SCALE / 2, position.y - itemWidth * MyMap.UNIT_SCALE / 2);
        sprite.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        sprite.setOriginCenter();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getItemWidth() {
        return itemWidth;
    }
}
