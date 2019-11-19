package com.light.v1;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.light.v1.ECS.*;
import com.light.v1.element.Sign;
import com.light.v1.tools.MyMap;

public class LightMap {
    private static final String TAG = "LightMap";
    private static MyMap _mapMgr = null;
    private OrthographicCamera camera;
    private static SystemManager systemManager=SystemManager.getInstance();


    public LightMap() {
        _mapMgr = new MyMap();
    }

    public boolean buildMap(World world, RayHandler rayHandler) {
        return buildLayer(world, rayHandler, _mapMgr.getCollisionLayer()) && buildLayer(world, rayHandler, _mapMgr.getInteractionLayer());
    }

    private boolean buildLayer(World world, RayHandler rayHandler, MapLayer layer) {
        if (layer == null) {
            Gdx.app.debug(TAG, "FALSE! ");
            return false;
        }

        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                createRectangle(world, rayHandler, ((RectangleMapObject) object));
            } else {
                MapProperties mp = object.getProperties();
                createPolygon(world, rayHandler, ((PolygonMapObject) object).getPolygon(), Float.parseFloat(mp.get("x").toString()), Float.parseFloat(mp.get("y").toString()));
            }
        }

        return true;
    }

    private void createPolygon(World world, RayHandler rayHandler, Polygon polygon, float x, float y) {
        float[] tmp = polygon.getVertices();
        Vector2[] vertices = new Vector2[tmp.length / 2];

        for (int i = 0; i < tmp.length; i += 2) {
            vertices[i / 2] = new Vector2(tmp[i] * MyMap.UNIT_SCALE, (tmp[i + 1] * MyMap.UNIT_SCALE));
        }

        BodyDef staticBodyDef = new BodyDef();
        staticBodyDef.type = BodyDef.BodyType.StaticBody;
        Body boxBody0 = world.createBody(staticBodyDef);
        ChainShape shape = new ChainShape();
        shape.createLoop(vertices);
        boxBody0.createFixture(shape, 1f);
        shape.dispose();
        boxBody0.setTransform(new Vector2(x * MyMap.UNIT_SCALE, y * MyMap.UNIT_SCALE), 0f);
        boxBody0.setUserData("Polygon");
    }

    private void createRectangle(World world, RayHandler rayHandler, RectangleMapObject object) {
        Rectangle rectangle=object.getRectangle();
        MapProperties mp=object.getProperties();
        BodyDef staticBodyDef = new BodyDef();
        //staticBodyDef.type = BodyDef.BodyType.StaticBody;
        String name="Rectangle "+ MathUtils.random(0, 10000);
        Sign sign=null;

        if (mp.containsKey("type") && mp.get("type").toString().compareTo("ennemy") == 0) {
            staticBodyDef.type = BodyDef.BodyType.DynamicBody;
            name="Ennemy "+MathUtils.random(0, 10000);
            createEnemy(world, rayHandler, rectangle);
            return ;
        }
        else if (mp.containsKey("type") && mp.get("type").toString().compareTo("warp") == 0) {
            staticBodyDef.type = BodyDef.BodyType.KinematicBody;
            name="Warp "+MathUtils.random(0, 10000);
        }
        else if (mp.containsKey("type") && mp.get("type").toString().compareTo("info") == 0) {
            staticBodyDef.type = BodyDef.BodyType.StaticBody;
            name="Info "+MathUtils.random(0, 10000);
            sign=new Sign(name, mp.get("message").toString());
        }
        else {
            staticBodyDef.type = BodyDef.BodyType.StaticBody;
        }

        Body boxBody2 = world.createBody(staticBodyDef);
        PolygonShape box2 = new PolygonShape();
        box2.setAsBox(rectangle.getWidth() * MyMap.UNIT_SCALE / 2, rectangle.getHeight() * MyMap.UNIT_SCALE / 2);
        FixtureDef fixture=new FixtureDef();
        fixture.restitution = 0.1f;
        fixture.friction=100f;
        fixture.density = 100f;
        fixture.shape = box2;

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

    public static MyMap getMap() {
        return _mapMgr;
    }

    public void createEnemy(World world, RayHandler rayHandler, Rectangle rectangle) {
        LightEnemyEntity entity =new LightEnemyEntity(rayHandler, camera, world);
        LightEnemyGraphics lightGraphics=new LightEnemyGraphics(entity);
        lightGraphics.addItem(world, rayHandler, rectangle);

        systemManager.addEntity(entity);
        systemManager.addEntityComponent(entity, new LightEnemyPhysics(entity));
        systemManager.addEntityComponent(entity, lightGraphics);
    }

    public int[] getBackLayers() {
        return _mapMgr.getBackLayers();
    }

    public int[] getFrontLayers() {
        return _mapMgr.getFrontLayers();
    }
}
