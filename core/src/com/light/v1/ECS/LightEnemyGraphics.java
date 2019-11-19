package com.light.v1.ECS;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.light.v1.tools.MyMap;

public class LightEnemyGraphics extends LightGraphics {
    private static final String TAG = "LightEnemyGraphics";

    private Sprite item = null;
    private float itemWidth=16;
    private Body bodyItem = null;

    private Vector2 currentPosition;
    private Vector2 startPosition;
    private Vector2[] positions=new Vector2[4];
    private int currentIndex=0;

    private float velocity=0.2f;

    private LightEnemyEntity enemy;

    public LightEnemyGraphics(LightEnemyEntity entity) {
        enemy=entity;
    }

    @Override
    public void update(Batch batch) {
        item.setPosition(bodyItem.getPosition().x - 16 * MyMap.UNIT_SCALE / 2, bodyItem.getPosition().y - 16 * MyMap.UNIT_SCALE / 2);
        item.setOriginCenter();
        item.setRotation(bodyItem.getAngle()*180f/(float)Math.PI);
    }

    public void render(Batch batch) {
        batch.begin();
        ((Sprite)bodyItem.getUserData()).draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "dispose");
    }

    @Override
    public void receiveMessage(ECSEvent.EVENT event, String message) {
    }

    public void addItem(World world, RayHandler rayHandler, Rectangle rectangle) {
        Texture texture = new Texture(Gdx.files.internal("enemy.png"));
        item = new Sprite(texture);
        item.setSize(item.getWidth() * MyMap.UNIT_SCALE, item.getHeight() * MyMap.UNIT_SCALE);

        PolygonShape boxItem = new PolygonShape();
        boxItem.setAsBox(rectangle.getWidth() * MyMap.UNIT_SCALE / 2, rectangle.getHeight() * MyMap.UNIT_SCALE / 2);

        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.position.set(rectangle.getX() * MyMap.UNIT_SCALE + rectangle.getWidth() * MyMap.UNIT_SCALE / 2, rectangle.getY() * MyMap.UNIT_SCALE + rectangle.getHeight() * MyMap.UNIT_SCALE / 2);
        boxBodyDef.type = BodyDef.BodyType.DynamicBody;

        PolygonShape box2 = new PolygonShape();
        box2.setAsBox(rectangle.getWidth() * MyMap.UNIT_SCALE / 2, rectangle.getHeight() * MyMap.UNIT_SCALE / 2);

        FixtureDef fixture=new FixtureDef();
        fixture.restitution = 0.1f;
        fixture.friction=100f;
        fixture.density = 100f;
        fixture.shape = box2;

        bodyItem=world.createBody(boxBodyDef);
        bodyItem.setLinearDamping(2f);
        bodyItem.setAngularDamping(10f);
        bodyItem.createFixture(fixture);

        box2.dispose();

        //bodyItem.setTransform(new Vector2(rectangle.getX() * MyMap.UNIT_SCALE + rectangle.getWidth() * MyMap.UNIT_SCALE / 2, rectangle.getY() * MyMap.UNIT_SCALE + rectangle.getHeight() * MyMap.UNIT_SCALE / 2), 0);
        bodyItem.setUserData(item);

        item.setPosition(bodyItem.getPosition().x - itemWidth * MyMap.UNIT_SCALE / 2, bodyItem.getPosition().y - itemWidth * MyMap.UNIT_SCALE / 2);
        item.setBounds(item.getX(), item.getY(), item.getWidth(), item.getHeight());

        startPosition=bodyItem.getPosition();
    }

    private void initPositions() {
        positions[0]=startPosition;
        positions[1]=new Vector2(startPosition.x+3, startPosition.y);
        positions[2]=new Vector2(startPosition.x+3, startPosition.y+3);
        positions[3]=new Vector2(startPosition.x, startPosition.y+3);
    }

    public Vector2 getPosition() {
        return bodyItem.getPosition();
    }


}
