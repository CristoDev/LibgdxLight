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
    private ECSEvent.AnimationDirection[] directions= new ECSEvent.AnimationDirection[4];
    private int currentIndex=0;
    private float velocity=0.4f;
    private Vector2 currentVelocity;
    private Vector2 deltaPosition;
    private ECSEvent.AnimationDirection currentDirection;

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
    }

    @Override
    public void render(SpriteBatch batch) {
        // render
    }

    public void addItem(Rectangle rectangle) {
        PolygonShape boxItem = new PolygonShape();
        boxItem.setAsBox(rectangle.getWidth() * MyMap.UNIT_SCALE / 2 , rectangle.getHeight() * MyMap.UNIT_SCALE / 2, new Vector2(rectangle.getWidth() * MyMap.UNIT_SCALE / 2, 0), 0);

        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.position.set(rectangle.getX() * MyMap.UNIT_SCALE, rectangle.getY() * MyMap.UNIT_SCALE);
        boxBodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyItem=entity.getWorld().createBody(boxBodyDef);
        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.filter.categoryBits = ECSFilter.ENEMY;
        fixtureDef.filter.maskBits = ECSFilter.MASK_ENEMY;
        fixtureDef.restitution = 0.1f;
        fixtureDef.friction=100f;
        fixtureDef.density = 100f;
        fixtureDef.shape = boxItem;

        bodyItem.createFixture(fixtureDef);
        bodyItem.setLinearDamping(2f);
        bodyItem.setAngularDamping(10f);
        bodyItem.setUserData(entity);
        startPosition=bodyItem.getPosition();

        boxItem.dispose();
    }

    private void initPositions() {
        positions[0]=startPosition.cpy();
        directions[0]= ECSEvent.AnimationDirection.RIGHT;
        positions[1]=new Vector2(startPosition.x+1, startPosition.y);
        directions[1]= ECSEvent.AnimationDirection.UP;
        positions[2]=new Vector2(startPosition.x+1, startPosition.y+1);
        directions[2]= ECSEvent.AnimationDirection.LEFT;
        positions[3]=new Vector2(startPosition.x, startPosition.y+1);
        directions[3]= ECSEvent.AnimationDirection.DOWN;
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
        currentDirection=directions[currentIndex];
        goalPosition=positions[(currentIndex+1)%positions.length];
        deltaPosition=goalPosition.cpy().sub(pStart);
        Vector2 deltaNorme=deltaPosition.cpy().nor();
        currentVelocity=new Vector2(deltaNorme.x * velocity, deltaNorme.y * velocity);
        SystemManager.getInstance().sendMessage(entity, ECSEvent.Event.SET_DIRECTION, currentDirection.toString());
    }
}
