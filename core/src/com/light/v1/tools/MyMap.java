package com.light.v1.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.util.HashMap;

public class MyMap {
    private String mapName="test.tmx";
    //private String currentMapName ="";
    private TiledMap currentMap =null;
    private HashMap<String, MapLayer> mapLayers=new HashMap<String, MapLayer>();

    private final static String WALL_OBJECT_LAYER = "wall_object";
    private final static String OBSTACLE_OBJECT_LAYER = "obstacle_object";
    private final static String FLOOR_OBJECT_LAYER = "floor_object";
    private final static String ENEMY_OBJECT_LAYER = "enemy_object";
    private final static String INTERACTION_OBJECT_LAYER="interaction_object";

    private final static String BACKGROUND_TILE_LAYER="background_tile";
    private final static String WALL_TILE_LAYER="wall_tile";
    //private final static String COLLISION_TILE_LAYER="collision_tile";
    //private final static String ENEMY_TILE_LAYER="enemy_tile";
    private final static String FRONT_TILE_LAYER = "front_tile";

    private HashMap<String, Integer> mapsIDs=new HashMap<String, Integer>();

    private String[] back={BACKGROUND_TILE_LAYER, WALL_TILE_LAYER};
    private String[] front={FRONT_TILE_LAYER};

    private static final String TAG = MyMap.class.getSimpleName();
    private static InternalFileHandleResolver _filePathResolver = new InternalFileHandleResolver();
    public static final AssetManager _assetManager = new AssetManager();

    public final static float UNIT_SCALE  = 1/16f;

    public MyMap() {
        init(mapName);
    }

    private void init(String _mapName) {
        loadMapAsset(_mapName);
    }

    public void loadMapAsset(String mapFilenamePath) {
        if (mapFilenamePath == null || mapFilenamePath.isEmpty()) {
            Gdx.app.debug(TAG, "file null");
            return;
        }

        if (_filePathResolver.resolve(mapFilenamePath).exists()) {
            _assetManager.setLoader(
                    TiledMap.class,
                    new TmxMapLoader(_filePathResolver)
            );

            _assetManager.load(mapFilenamePath, TiledMap.class);

            // tant qu'on charge la map, on bloque la suite du jeu
            _assetManager.finishLoadingAsset(mapFilenamePath);
        } else {
            Gdx.app.debug(TAG, "map doesn't exists! " + mapFilenamePath);
        }
    }

    public static TiledMap getMapAsset(String mapFilenamePath) {
        TiledMap map = null;

        if (_assetManager.isLoaded(mapFilenamePath)) {
            map=_assetManager.get(mapFilenamePath, TiledMap.class);
        }
        else {
            Gdx.app.debug(TAG, "Map is not loaded "+ mapFilenamePath);
        }

        return map;
    }

    public void loadMap(String mapName){
        String currentMapName ="";

        if( mapName == null || mapName.isEmpty() ) {
            Gdx.app.debug(TAG, "Map is invalid");
            return;
        }

        if( currentMap != null ){
            currentMap.dispose();
        }

        loadMapAsset(mapName);
        if( isAssetLoaded(mapName) ) {
            currentMap = getMapAsset(mapName);
            currentMapName = mapName;
        }else{
            Gdx.app.debug(TAG, "Map not loaded");
            return;
        }

        MapLayers maps= currentMap.getLayers();

        for (int i=0; i<maps.size(); i++) {
            mapLayers.put(maps.get(i).getName(), maps.get(i));
            mapsIDs.put(maps.get(i).getName(), maps.getIndex(maps.get(i)));
        }
    }

    public static boolean isAssetLoaded(String filename) {
        return _assetManager.isLoaded(filename);
    }

    public TiledMap getCurrentMap(){
        if( currentMap == null ) {
            loadMap(mapName);
        }

        return currentMap;
    }

    private MapLayer getLayer(String name) {
        return mapLayers.get(name);
    }

    public MapLayer getWallLayer() {
        return getLayer(WALL_OBJECT_LAYER);
    }

    public MapLayer getObstacleLayer() {
        return getLayer(OBSTACLE_OBJECT_LAYER);
    }

    public MapLayer getFloorLayer() {
        return getLayer(FLOOR_OBJECT_LAYER);
    }

    public MapLayer getInteractionLayer() {
        return getLayer(INTERACTION_OBJECT_LAYER);
    }

    public MapLayer getEnemyLayer() {
        return getLayer(ENEMY_OBJECT_LAYER);
    }

    private int[] getLayers(String[] layers) {
        int[] result=new int[layers.length];

        for (int i=0; i<layers.length; i++) {
            result[i]=mapsIDs.get(layers[i]);
        }

        return result;

    }

    public int[] getBackLayers() {
        return getLayers(back);
    }

    public int[] getFrontLayers() {
        return getLayers(front);
    }
}
