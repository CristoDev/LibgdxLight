package com.light.v1.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.light.v1.tools.MyMap;

public class LightEnemyPhysics extends LightPhysics {
    private static final String TAG = "LightEnemyPhysics";

    private Body bodyItem = null;
    private Vector2 startPosition;
    private Vector2 goalPosition;
    private Vector2[] positions=new Vector2[4];
    private int currentIndex=0;
    private float velocity=0.4f;
    private Vector2 currentVelocity;
    private Vector2 deltaPosition;
    //private Vector2 deltaNorme;

    public LightEnemyPhysics(LightEnemyEntity entity) {
        this.entity=entity;
    }

    public LightEnemyPhysics(LightEnemyEntity entity, Rectangle rectangle) {
        this.entity=entity;
        addItem(rectangle);
        initPositions();
        updateVelocity();
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "dispose");
    }

    @Override
    public void receiveMessage(ECSEvent.Event event, String message) {
        // receive message
    }

    @Override
    public void update(float delta) {
        if (isChangingPosition()) {
            currentIndex=(currentIndex+1)%positions.length;
            updateVelocity();
        }

        bodyItem.setLinearVelocity(currentVelocity);
        SystemManager.getInstance().sendMessage(entity, ECSEvent.Event.SET_POSITION, bodyItem.getPosition().x+ECSEvent.MESSAGE_TOKEN+bodyItem.getPosition().y);
        //entity.setPosition(bodyItem.getPosition());

        //entity.getSprite().setRotation(bodyItem.getAngle()*180f/(float)Math.PI);
    }

    @Override
    public void render(SpriteBatch batch) {
        // render
    }

    public void addItem(Rectangle rectangle) {
        PolygonShape boxItem = new PolygonShape();
        boxItem.setAsBox(rectangle.getWidth() * MyMap.UNIT_SCALE / 2, rectangle.getHeight() * MyMap.UNIT_SCALE / 2);

        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.position.set(rectangle.getX() * MyMap.UNIT_SCALE + rectangle.getWidth() * MyMap.UNIT_SCALE / 2, rectangle.getY() * MyMap.UNIT_SCALE + rectangle.getHeight() * MyMap.UNIT_SCALE / 2);
        boxBodyDef.type = BodyDef.BodyType.DynamicBody;

        PolygonShape box2 = new PolygonShape();
        box2.setAsBox(rectangle.getWidth() * MyMap.UNIT_SCALE / 2, rectangle.getHeight() * MyMap.UNIT_SCALE / 2);

        FixtureDef fixture=new FixtureDef();
        fixture.filter.categoryBits = ECSFilter.ENEMY;
        fixture.filter.maskBits = ECSFilter.MASK_ENEMY;
        fixture.restitution = 0.1f;
        fixture.friction=100f;
        fixture.density = 100f;
        fixture.shape = box2;

        bodyItem=entity.getWorld().createBody(boxBodyDef);
        bodyItem.setLinearDamping(2f);
        bodyItem.setAngularDamping(10f);
        bodyItem.createFixture(fixture);

        box2.dispose();

        bodyItem.setUserData(entity);
        startPosition=bodyItem.getPosition();
    }

    private void initPositions() {
        positions[0]=startPosition.cpy();
        positions[1]=new Vector2(startPosition.x+1, startPosition.y);
        positions[2]=new Vector2(startPosition.x+1, startPosition.y+1);
        positions[3]=new Vector2(startPosition.x, startPosition.y+1);
    }

    private boolean isChangingPosition() {
        if (deltaPosition.x > 0) {
            return (bodyItem.getPosition().x > goalPosition.x);
        }

        if (deltaPosition.x < 0) {
            return (bodyItem.getPosition().x < goalPosition.x);
        }

        if (deltaPosition.y > 0) {
            return (bodyItem.getPosition().y > goalPosition.y);
        }

        if (deltaPosition.y < 0) {
            return (bodyItem.getPosition().y < goalPosition.y);
        }

        return false;
    }

    private void updateVelocity() {
        Vector2 pStart=positions[currentIndex];
        goalPosition=positions[(currentIndex+1)%positions.length];
        deltaPosition=goalPosition.cpy().sub(pStart);
        Vector2 deltaNorme=deltaPosition.cpy().nor();
        currentVelocity=new Vector2(deltaNorme.x * velocity, deltaNorme.y * velocity);
    }
}
