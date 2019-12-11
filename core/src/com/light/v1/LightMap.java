package com.light.v1;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.*;
import com.light.v1.tools.LightFactory;
import com.light.v1.tools.MyMap;

public class LightMap {
    private static final String TAG = "LightMap";
    private static MyMap _mapMgr = null;
    private OrthographicCamera camera;
    private static LightFactory lightFactory=LightFactory.getInstance();


    public LightMap(OrthographicCamera camera) {
        this.camera=camera;
        _mapMgr = new MyMap();
    }

    public boolean buildMap(World world, RayHandler rayHandler) {
        lightFactory.init(world, rayHandler);
        return buildObjectsLayers(world, rayHandler);
    }

    private boolean buildObjectsLayers(World world, RayHandler rayHandler) {
        return buildLayer(world, rayHandler, _mapMgr.getCollisionLayer()) && buildLayer(world, rayHandler, _mapMgr.getFloorLayer(), true)  && buildLayer(world, rayHandler, _mapMgr.getInteractionLayer());
    }

    private boolean buildLayer(World world, RayHandler rayHandler, MapLayer layer) {
        return buildLayer(world, rayHandler, layer,false);
    }

    private boolean buildLayer(World world, RayHandler rayHandler, MapLayer layer, boolean isSensor) {
        if (layer == null) {
            return false;
        }

        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                lightFactory.createLightObject(camera, ((RectangleMapObject) object).getRectangle(), object.getProperties(), isSensor);
            } else {
                lightFactory.createLightObject(camera, ((PolygonMapObject) object).getPolygon(), object.getProperties(), isSensor);
            }
        }

        return true;
    }

    public static MyMap getMap() {
        return _mapMgr;
    }

    public int[] getBackLayers() {
        return _mapMgr.getBackLayers();
    }

    public int[] getFrontLayers() {
        return _mapMgr.getFrontLayers();
    }
}
