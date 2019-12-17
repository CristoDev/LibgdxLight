package com.light.v1;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.*;
import com.light.v1.ecs.ECSFilter;
import com.light.v1.tools.LightFactory;
import com.light.v1.tools.MyMap;

public class LightMap {
    //private static final String TAG = "LightMap";
    private static MyMap _mapMgr = null;
    private OrthographicCamera camera;
    private static LightFactory lightFactory=LightFactory.getInstance();


    public LightMap(OrthographicCamera camera) {
        this.camera=camera;
        _mapMgr = new MyMap();
    }

    public void buildMap(World world, RayHandler rayHandler) {
        lightFactory.init(world, rayHandler);

        buildLayer(_mapMgr.getWallLayer(), false, ECSFilter.WALL, ECSFilter.MASK_ALL);
        buildLayer(_mapMgr.getObstacleLayer(), false, ECSFilter.OBSTACLE, ECSFilter.MASK_OBSTACLE);
        buildEnnemyLayer(_mapMgr.getEnemyLayer());
        buildLayer(_mapMgr.getFloorLayer(), true, ECSFilter.FLOOR, ECSFilter.MASK_ALL);
        buildLayer(_mapMgr.getInteractionLayer(), false, ECSFilter.OBSTACLE, ECSFilter.MASK_OBSTACLE);
    }

    private void buildEnnemyLayer(MapLayer layer) {
        if (layer == null) {
            return ;
        }

        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                lightFactory.createLightEnemy(camera, ((RectangleMapObject) object).getRectangle(), object.getProperties());
            }
        }
    }

    private boolean buildLayer(MapLayer layer, boolean isSensor, short category, short mask) {
        if (layer == null) {
            return false;
        }

        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                lightFactory.createLightObject(camera, ((RectangleMapObject) object).getRectangle(), object.getProperties(), isSensor, category, mask);
            } else {
                lightFactory.createLightObject(camera, ((PolygonMapObject) object).getPolygon(), object.getProperties(), isSensor, category, mask);
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
