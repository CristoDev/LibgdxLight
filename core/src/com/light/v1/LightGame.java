package com.light.v1;

import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.light.v1.ecs.ECSEvent;
import com.light.v1.ecs.LightPlayerEntity;
import com.light.v1.ecs.SystemManager;
import com.light.v1.tools.ContactManager;
import com.light.v1.tools.LightFactory;
import com.light.v1.tools.MyMap;

public class LightGame implements ApplicationListener {
    //private static final String TAG = "Box2DLightsSample";

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private World world;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Box2DDebugRenderer debugRenderer;
    private RayHandler rayHandler;
    private static SystemManager systemManager=SystemManager.getInstance();
    private LightFactory lightFactory=LightFactory.getInstance();
    //private static WorldManager worldManager=WorldManager.getInstance();

    public static class ViewportUtils { // classe public au lieu de private en attendant le refactoring complet
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
        camera.setToOrtho(false, ViewportUtils.viewportWidth, ViewportUtils.viewportHeight);
        camera.position.set(ViewportUtils.viewportWidth / 2f, ViewportUtils.viewportHeight / 2, 0);
        camera.update();

        batch = new SpriteBatch();

        world = new World(new Vector2(0, 0f), true);
        world.setContactListener(new ContactManager());

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.1f, 0.1f, 0.1f, 0.7f);//ne modifier que alpha

        batch.setProjectionMatrix(camera.combined);

        debugRenderer = new Box2DDebugRenderer();

        lightFactory.init(world, rayHandler, camera);
        mapRenderer = new OrthogonalTiledMapRenderer(LightFactory.getMap().getCurrentMap(), MyMap.UNIT_SCALE);
        lightFactory.buildMap();

        LightPlayerEntity lightPlayerEntity;
        lightPlayerEntity=LightFactory.getInstance().createLightPlayer(camera);
        lightPlayerEntity.createLights();

        test();
    }

    private  void test() {

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

    private void update(float delta) {
        //camera.position.set(lightPlayerEntity.getPosition().x, lightPlayerEntity.getPosition().y, 0);
        world.step(delta, 8, 3);
        systemManager.update(delta);
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(camera);
        mapRenderer.render(lightFactory.getBackLayers());

        batch.setProjectionMatrix(camera.combined);
        systemManager.render(batch);

        batch.begin();
        camera.update();
        batch.end();

        mapRenderer.render(lightFactory.getFrontLayers());

        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void pause() {
        // pause
    }

    @Override
    public void resume() {
        // resume
    }

    public void setupViewport(int width, int height) {
        ViewportUtils.virtualWidth = width;
        ViewportUtils.virtualHeight = height;
        ViewportUtils.viewportWidth = ViewportUtils.virtualWidth;
        ViewportUtils.viewportHeight = ViewportUtils.virtualHeight;
        ViewportUtils.physicalWidth = Gdx.graphics.getWidth();
        ViewportUtils.physicalHeight = Gdx.graphics.getHeight();
        ViewportUtils.aspectRatio = (ViewportUtils.virtualWidth / ViewportUtils.virtualHeight);

        if (ViewportUtils.physicalWidth / ViewportUtils.physicalHeight >= ViewportUtils.aspectRatio) {
            ViewportUtils.viewportWidth = ViewportUtils.viewportHeight * (ViewportUtils.physicalWidth / ViewportUtils.physicalHeight);
            ViewportUtils.viewportHeight = ViewportUtils.virtualHeight;
        } else {
            ViewportUtils.viewportWidth = ViewportUtils.virtualWidth;
            ViewportUtils.viewportHeight = ViewportUtils.viewportWidth * (ViewportUtils.physicalHeight / ViewportUtils.physicalWidth);
        }
    }

    public Vector3 unproject(Vector3 point) {
        return camera.unproject(point);
    }

    public Vector3 unproject(float x, float y, float z) {
        return unproject(new Vector3(x, y, z));
    }

}