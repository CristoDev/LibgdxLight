package com.light.v1;

import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import tools.ContactManager;
import tools.MyMap;

public class LightGame implements ApplicationListener {
    private static final String TAG = "Box2DLightsSample";

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private World world;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Box2DDebugRenderer debugRenderer;
    private RayHandler rayHandler;

    private LightMap lightMap;
    private LightPlayer lightPlayer;
    private ObserverNotifier observerManager;

    public static class VIEWPORT { // classe public au lieu de private en attendant le refactoring complet
        static float viewportWidth;
        static float viewportHeight;
        static float virtualWidth;
        static float virtualHeight;
        static float physicalWidth;
        static float physicalHeight;
        static float aspectRatio;
    }

    @Override
    public void create() {
        setupViewport(20, 20);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
        camera.position.set(VIEWPORT.viewportWidth / 2f, VIEWPORT.viewportHeight / 2, 0);
        camera.update();

        batch = new SpriteBatch();

        world = new World(new Vector2(0, 0f), true);
        world.setContactListener(new ContactManager());

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.2f, 0.2f, 0.2f, 0.6f);//0.6f

        batch.setProjectionMatrix(camera.combined);

        debugRenderer = new Box2DDebugRenderer();

        lightPlayer=new LightPlayer(rayHandler, camera, world);
        lightPlayer.createLights();

        lightMap=new LightMap();
        mapRenderer = new OrthogonalTiledMapRenderer(lightMap.getMap().getCurrentMap(), MyMap.UNIT_SCALE);
        lightMap.buildMap(world);

        observerManager=new ObserverNotifier();
        observerManager.addObserver(lightPlayer);
    }

    @Override
    public void resize(int width, int height) {
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void dispose() {
        debugRenderer.dispose();

        batch.dispose();
        rayHandler.dispose();
        world.dispose();
    }

    private void update() {
        camera.position.set(lightPlayer.getPosition().x, lightPlayer.getPosition().y, 0);
        lightPlayer.update();
        camera.update();
        world.step(Gdx.graphics.getDeltaTime(), 8, 3);
    }

    @Override
    public void render() {
        update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        lightPlayer.render(batch);
        batch.end();

        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    private void setupViewport(int width, int height) {
        VIEWPORT.virtualWidth = width;
        VIEWPORT.virtualHeight = height;
        VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
        VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
        VIEWPORT.physicalHeight = Gdx.graphics.getHeight();
        VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth / VIEWPORT.virtualHeight);

        if (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio) {
            VIEWPORT.viewportWidth = VIEWPORT.viewportHeight * (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight);
            VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        } else {
            VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
            VIEWPORT.viewportHeight = VIEWPORT.viewportWidth * (VIEWPORT.physicalHeight / VIEWPORT.physicalWidth);
        }
    }

    public Vector3 unproject(Vector3 point) {
        return camera.unproject(point);
    }

    public Vector3 unproject(float x, float y, float z) {
        return unproject(new Vector3(x, y, z));
    }

}