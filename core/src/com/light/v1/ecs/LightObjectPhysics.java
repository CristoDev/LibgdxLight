package com.light.v1.ecs;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.light.v1.element.Sign;
import com.light.v1.tools.LightFactory;
import com.light.v1.tools.MyMap;

public class LightObjectPhysics extends LightPhysics {
    private static final String TAG = "LightObjectPhysics";

    public LightObjectPhysics(LightObjectEntity entity, World world, RayHandler rayHandler, Rectangle rectangle, MapProperties mapProperties, boolean isSensor) {
        this.entity=entity;
        this.entity.setType(mapProperties);
        createRectangle(world, rayHandler, rectangle, mapProperties, isSensor);
    }

    public LightObjectPhysics(LightObjectEntity entity, World world, Polygon polygon, MapProperties mapProperties, boolean isSensor) {
        this.entity=entity;
        this.entity.setType(mapProperties);
        createPolygon(world, polygon, mapProperties, isSensor);
    }

    private void createRectangle(World world, RayHandler rayHandler, Rectangle rectangle, MapProperties mapProperties, boolean isSensor) {
        BodyDef staticBodyDef = new BodyDef();
        String name="Rectangle "+ MathUtils.random(0, 10000);
        Sign sign=null;

        PolygonShape box2 = new PolygonShape();
        box2.setAsBox(rectangle.getWidth() * MyMap.UNIT_SCALE / 2, rectangle.getHeight() * MyMap.UNIT_SCALE / 2);

        FixtureDef fixture=new FixtureDef();
        fixture.restitution = 0.1f;
        fixture.friction=100f;
        fixture.density = 100f;
        fixture.shape = box2;
        fixture.isSensor=isSensor;

        if (mapProperties.containsKey("type") && mapProperties.get("type").toString().compareTo("warp") == 0) {
            staticBodyDef.type = BodyDef.BodyType.KinematicBody;
            name="Warp "+MathUtils.random(0, 10000);
        }
        else if (mapProperties.containsKey("type") && mapProperties.get("type").toString().compareTo("water") == 0) {
            Gdx.app.debug(TAG, "create rectangle for " +mapProperties.get("type").toString());
            staticBodyDef.type = BodyDef.BodyType.StaticBody;
            fixture.isSensor=true;
            name="Water "+MathUtils.random(0, 10000);
        }
        else if (mapProperties.containsKey("type") && mapProperties.get("type").toString().compareTo("grass") == 0) {
            staticBodyDef.type = BodyDef.BodyType.StaticBody;
            fixture.isSensor=true;
            name="Grass "+MathUtils.random(0, 10000);
        }
        else if (mapProperties.containsKey("type") && mapProperties.get("type").toString().compareTo("info") == 0) {
            staticBodyDef.type = BodyDef.BodyType.StaticBody;
            name="Info "+MathUtils.random(0, 10000);
            sign=new Sign(name, mapProperties.get("message").toString());
        }
        else {
            staticBodyDef.type = BodyDef.BodyType.StaticBody;
        }

        Body boxBody2 = world.createBody(staticBodyDef);

        boxBody2.setLinearDamping(2f);
        boxBody2.setAngularDamping(10f);
        boxBody2.createFixture(fixture);
        box2.dispose();
        boxBody2.setTransform(new Vector2(rectangle.getX() * MyMap.UNIT_SCALE + rectangle.getWidth() * MyMap.UNIT_SCALE / 2, rectangle.getY() * MyMap.UNIT_SCALE + rectangle.getHeight() * MyMap.UNIT_SCALE / 2), 0);

        if (sign != null) {
            boxBody2.setUserData(sign);
        }
        else {
            boxBody2.setUserData(name);
        }

    }

    private void createPolygon(World world, Polygon polygon, MapProperties mapProperties, boolean isSensor) {
        float[] tmp = polygon.getVertices();
        Vector2[] vertices = new Vector2[tmp.length / 2];

        for (int i = 0; i < tmp.length; i += 2) {
            vertices[i / 2] = new Vector2(tmp[i] * MyMap.UNIT_SCALE, (tmp[i + 1] * MyMap.UNIT_SCALE));
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);
        ChainShape shape = new ChainShape();
        shape.createLoop(vertices);

        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.shape=shape;
        fixtureDef.isSensor=isSensor;
        fixtureDef.density=1f;

        body.createFixture(fixtureDef);
        shape.dispose();
        body.setTransform(
                new Vector2(
                        Float.parseFloat(mapProperties.get("x").toString()) * MyMap.UNIT_SCALE,
                        Float.parseFloat(mapProperties.get("y").toString()) * MyMap.UNIT_SCALE),
                0f);
        body.setUserData(entity);
    }


    @Override
    public void dispose() {
        // dispose
    }

    @Override
    public void receiveMessage(ECSEvent.Event event, String message) {
        // receiveMessage
    }

    @Override
    public void update(Batch batch) {
        // update
    }

    @Override
    public void render(Batch batch) {
        // render
    }
}
