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
import com.light.v1.ECS.*;
import com.light.v1.tools.ContactManager;
import com.light.v1.tools.MyMap;

import java.util.ArrayList;

public class LightGame implements ApplicationListener {
    private static final String TAG = "Box2DLightsSample";

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private World world;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Box2DDebugRenderer debugRenderer;
    private RayHandler rayHandler;
    private static SystemManager systemManager=SystemManager.getInstance();

    private LightMap lightMap;
    private LightPlayerEntity lightPlayerEntity;

    public static class VIEWPORT { // classe public au lieu de private en attendant le refactoring complet
        public static float viewportWidth;
        public static float viewportHeight;
        public static float virtualWidth;
        public static float virtualHeight;
        public static float physicalWidth;
        public static float physicalHeight;
        public static float aspectRatio;
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

        lightMap=new LightMap();
        mapRenderer = new OrthogonalTiledMapRenderer(lightMap.getMap().getCurrentMap(), MyMap.UNIT_SCALE);
        lightMap.buildMap(world);

        createPlayer();
        createPlayerOptional();
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
        camera.position.set(lightPlayerEntity.getPosition().x, lightPlayerEntity.getPosition().y, 0);
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
        systemManager.update(lightPlayerEntity, batch);

        batch.begin();
        camera.update();
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

    public void createPlayer() {
        lightPlayerEntity =new LightPlayerEntity(rayHandler, camera, world);
        LightPlayerGraphics lightGraphics=new LightPlayerGraphics(lightPlayerEntity);
        lightGraphics.addItem(world, rayHandler);
        lightGraphics.createSword(world);

        systemManager.addEntity(lightPlayerEntity);
        systemManager.addEntityComponent(lightPlayerEntity, new LightPlayerInput(lightPlayerEntity));
        systemManager.addEntityComponent(lightPlayerEntity, lightGraphics);
    }

    private void createPlayerOptional() {
        lightPlayerEntity.createLights();
    }

    public void setupViewport(int width, int height) {
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