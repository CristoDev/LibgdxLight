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
    private String mapName="test.tmx", _currentMapName="";
    private TiledMap _currentMap=null;
    private HashMap<String, MapLayer> mapLayers=new HashMap<String, MapLayer>();

    private final static String COLLISION_LAYER = "collision";
    private final static String INTERACTION_LAYER="interaction";

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
            //Gdx.app.debug(TAG, "Map loaded " + mapFilenamePath);
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
        String mapFullPath = mapName;

        if( mapFullPath == null || mapFullPath.isEmpty() ) {
            Gdx.app.debug(TAG, "Map is invalid");
            return;
        }

        if( _currentMap != null ){
            _currentMap.dispose();
        }

        loadMapAsset(mapFullPath);
        if( isAssetLoaded(mapFullPath) ) {
            _currentMap = getMapAsset(mapFullPath);
            _currentMapName = mapName;
        }else{
            Gdx.app.debug(TAG, "Map not loaded");
            return;
        }

        MapLayers maps=_currentMap.getLayers();

        for (int i=0; i<maps.size(); i++) {
            mapLayers.put(maps.get(i).getName(), maps.get(i));
        }
    }

    public static boolean isAssetLoaded(String filename) {
        return _assetManager.isLoaded(filename);
    }

    public TiledMap getCurrentMap(){
        if( _currentMap == null ) {
            loadMap(mapName);
        }

        return _currentMap;
    }

    private MapLayer getLayer(String name) {
        return mapLayers.get(name);
    }

    public MapLayer getCollisionLayer() {
        return getLayer(COLLISION_LAYER);
    }

    public MapLayer getInteractionLayer() {
        return getLayer(INTERACTION_LAYER);
    }

}
