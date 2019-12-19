package com.light.v1.ecs;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.light.v1.tools.AnimationEntity;
import com.light.v1.tools.MyMap;

public class LightEntity {
    //private static final String TAG = "LightEntity";

    protected RayHandler rayHandler;
    protected OrthographicCamera camera;
    protected World world;

    //protected float velocity0=3f;
    //protected float velocity=velocity0;

    //protected Vector2 position=new Vector2(0, 0);
    //protected Sprite sprite;
    //private float itemWidth=16;
    private String type=null;
    protected MapProperties mapProperties;

    public LightEntity(World world, RayHandler rayHandler, OrthographicCamera camera) {
        this.rayHandler=rayHandler;
        this.camera=camera;
        this.world=world;
    }

    public LightEntity(World world, RayHandler rayHandler, OrthographicCamera camera, MapProperties mapProperties) {
        this.rayHandler=rayHandler;
        this.camera=camera;
        this.world=world;
        this.mapProperties=mapProperties;
    }

    /*
    public void setPosition(Vector2 _position) {
        position.x=_position.x - itemWidth * MyMap.UNIT_SCALE / 2;
        position.y=_position.y - itemWidth * MyMap.UNIT_SCALE / 2;

        if (sprite != null) {
            //Gdx.app.debug("POS", "position " + position.x+"/"+position.y);
            sprite.setPosition(position.x, position.y);
        }
    }

     */

    public void setType(MapProperties mapProperties) {
        if (mapProperties.containsKey("type")) {
            type=mapProperties.get("type").toString();
        }
    }

    public String getType() {
        return type;
    }

    public void setMapProperties(MapProperties map) {
        mapProperties=map;
        setType(map);
    }

    public MapProperties getMapProperties() {
        return mapProperties;
    }

    public int getPropertyId() {
        return Integer.parseInt(getProperty("id").toString());
    }

    public Object getProperty(String key) {
        Object property=null;
        if (mapProperties.containsKey(key)) {
            property=mapProperties.get(key);
        }

        return property;
    }

    public void update(Batch batch) {
        // update
    }

    public void render(Batch batch) {
        // render
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public World getWorld() {
        return world;
    }
}
