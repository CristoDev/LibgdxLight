package com.light.v1;

import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import tools.ContactManager;

public class Box2DLightsSample implements ApplicationListener {
    private static final String TAG = "Box2DLightsSample";

    private SpriteBatch batch = null;
    private OrthographicCamera camera = null;
    private World world = null;
    private Box2DDebugRenderer debugRenderer = null;
    private RayHandler rayHandler = null;

    private LightMap lightMap;
    private LightPlayer lightPlayer;

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

        createBodies();

        lightMap=new LightMap(camera);
        lightMap.createBox2DMapLayer(world);
    }


    private void createBodies() {
        BodyDef staticBodyDef = new BodyDef();
        staticBodyDef.type = BodyType.StaticBody;

        Body groundBody = world.createBody(staticBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(6, 0.5f);
        groundBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();
        groundBody.setTransform(new Vector2(6, 0.5f), groundBody.getAngle());
        groundBody.setUserData("Ground");

        Body boxBody = world.createBody(staticBodyDef);
        PolygonShape box = new PolygonShape();
        box.setAsBox(1f, .5f);
        boxBody.createFixture(box, 0.0f);
        box.dispose();
        // !!! milieu de la box
        boxBody.setTransform(new Vector2(6, 3), 0);
        boxBody.setUserData("Box "+MathUtils.random(0, 10000));
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

        lightMap.render(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //batch.draw(item, item.getX(), item.getY(), item.getWidth(), item.getHeight());
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



}