package com.light.v1.ecs;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.light.v1.tools.MyMap;

public abstract class LightPhysics implements Component {
    private static final String TAG = "LightPhysics";

    protected LightEntity entity;
    protected Short fixtureCategory=0;
    protected short fixtureMask=0;
    protected BodyDef bodyDef;
    protected FixtureDef fixtureDef;
    protected Vector2 bodyPosition=new Vector2(0, 0);

    protected void createObject(World world, RayHandler rayHandler, MapObject object, boolean isSensor) {
        initObject(object.getProperties(), isSensor);
        createShape(world, rayHandler, object);
    }

    private void initObject(MapProperties mapProperties, boolean isSensor) {
        bodyDef = new BodyDef();
        bodyDef.type= BodyDef.BodyType.StaticBody;

        fixtureDef=new FixtureDef();
        fixtureDef.filter.categoryBits=fixtureCategory;
        fixtureDef.filter.maskBits=fixtureMask;
        fixtureDef.restitution = 0.1f;
        fixtureDef.friction=100f;
        fixtureDef.density = 100f;
        fixtureDef.isSensor=isSensor;

        if (mapProperties.containsKey("type") && mapProperties.get("type").toString().startsWith("warp")) {
            bodyDef.type = BodyDef.BodyType.KinematicBody;
        }
        else if (mapProperties.containsKey("type") && mapProperties.get("type").toString().compareTo("info") == 0) {
            //sign=new Sign(name, mapProperties.get("message").toString());
            Gdx.app.debug(TAG, "@TODO sign to implement");
        }
    }

    private void createShape(World world, RayHandler rayHandler, MapObject object) {
        if (object instanceof RectangleMapObject) {
            fixtureDef.shape=createRectangle(object);
        }
        else if (object instanceof CircleMapObject) {
            fixtureDef.shape=createCircle(object);
        }
        else if (object instanceof EllipseMapObject) {
            fixtureDef.shape=createEllipse(object);
        }
        else if (object instanceof PolygonMapObject) {
            fixtureDef.shape=createPolygon(object);
        }
        else {
            Gdx.app.debug(TAG, object.getClass().getSimpleName() + " non implémentée");
            return ;
        }

        Body body=world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setTransform(bodyPosition, 0);
        body.setUserData(entity);
    }


    private Shape createRectangle(MapObject object) {
        Rectangle element=((RectangleMapObject) object).getRectangle();
        PolygonShape shape=new PolygonShape();
        shape.setAsBox(element.getWidth() * MyMap.UNIT_SCALE / 2, element.getHeight() * MyMap.UNIT_SCALE / 2);

        bodyPosition=new Vector2(element.getX() * MyMap.UNIT_SCALE + element.getWidth() * MyMap.UNIT_SCALE / 2,
                element.getY() * MyMap.UNIT_SCALE + element.getHeight() * MyMap.UNIT_SCALE / 2);

        return shape;
    }

    private Shape createCircle(MapObject object) {
        Circle element=((CircleMapObject) object).getCircle();
        CircleShape shape=new CircleShape();
        shape.setRadius(element.radius * MyMap.UNIT_SCALE /2);

        bodyPosition=new Vector2(element.x * MyMap.UNIT_SCALE + element.radius * MyMap.UNIT_SCALE / 2,
                element.y * MyMap.UNIT_SCALE + element.radius * MyMap.UNIT_SCALE / 2);

        return shape;
    }

    private Shape createEllipse(MapObject object) {
        Ellipse element=((EllipseMapObject) object).getEllipse();
        CircleShape shape=new CircleShape();
        shape.setRadius(((element.height+element.width)/2) * MyMap.UNIT_SCALE/2);

        bodyPosition=new Vector2(element.x * MyMap.UNIT_SCALE + element.height * MyMap.UNIT_SCALE / 2,
                element.y * MyMap.UNIT_SCALE + element.height * MyMap.UNIT_SCALE / 2);

        return shape;
    }

    private Shape createPolygon(MapObject object) {
        Polygon element = ((PolygonMapObject) object).getPolygon();
        float[] tmp = element.getVertices();

        Vector2[] vertices = new Vector2[tmp.length / 2];

        for (int i = 0; i < tmp.length; i += 2) {
            vertices[i / 2] = new Vector2(tmp[i] * MyMap.UNIT_SCALE, (tmp[i + 1] * MyMap.UNIT_SCALE));
        }

        ChainShape shape = new ChainShape();
        shape.createLoop(vertices);

        bodyPosition=new Vector2(Float.parseFloat(object.getProperties().get("x").toString()) * MyMap.UNIT_SCALE,
                Float.parseFloat(object.getProperties().get("y").toString()) * MyMap.UNIT_SCALE);

        return shape;
    }

}
