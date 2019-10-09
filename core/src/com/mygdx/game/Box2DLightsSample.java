package com.mygdx.game;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.TimeUtils;

public class Box2DLightsSample extends InputAdapter implements ApplicationListener {
    private static final String TAG = "Box2DLightsSample";

    private Vector3 point = new Vector3();
    private SpriteBatch batch = null;
    private OrthographicCamera camera = null;
    private World world = null;
    private Box2DDebugRenderer debugRenderer = null;
    private RayHandler rayHandler = null;
    private Light light = null, loop = null;
    private OrthogonalTiledMapRenderer _mapRenderer = null;
    private static MyMap _mapMgr = null;
    private Sprite item = null;
    private Body bodyItem = null;
    private Light lightItem=null;
    private long startTime, lastTime=0;
    private Vector2 translate = new Vector2(0, 0);
    private double angle=Math.PI/2;
    private float velocity=2f;

    private static class VIEWPORT {
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
        Gdx.input.setInputProcessor(this);
        startTime = TimeUtils.millis();

        world = new World(new Vector2(0, 0f), true);
        world.setContactListener(new ContactManager());
        // Instantiate the class in charge of drawing physics shapes
        debugRenderer = new Box2DDebugRenderer();
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.2f, 0.2f, 0.2f, 0.6f);

        batch.setProjectionMatrix(camera.combined);

        //createLights();
        createBodies();
        testMap();
        addItem();
    }

    private void createLights() {
        light = new PointLight(rayHandler, 32);
        light.setActive(false);
        light.setColor(Color.PURPLE);
        light.setDistance(5f);

        loop = new PointLight(rayHandler, 16, Color.YELLOW, 1f, 5, 5);
        loop.setActive(true);

        Light conelight = new ConeLight(rayHandler, 32, Color.WHITE, 20, 5, 5, 270, 45);
        conelight.setSoft(false);
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

    private void createBoxFromRectangleMap(Rectangle rectangle) {
        BodyDef staticBodyDef = new BodyDef();
        staticBodyDef.type = BodyType.StaticBody;

        Body boxBody2 = world.createBody(staticBodyDef);
        PolygonShape box2 = new PolygonShape();
        box2.setAsBox(rectangle.getWidth() * MyMap.UNIT_SCALE / 2, rectangle.getHeight() * MyMap.UNIT_SCALE / 2);
        boxBody2.createFixture(box2, 0.0f);
        box2.dispose();
        boxBody2.setTransform(new Vector2(rectangle.getX() * MyMap.UNIT_SCALE + rectangle.getWidth() * MyMap.UNIT_SCALE / 2, rectangle.getY() * MyMap.UNIT_SCALE + rectangle.getHeight() * MyMap.UNIT_SCALE / 2), 0);
        boxBody2.setUserData("Rectangle "+MathUtils.random(0, 10000));
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            //Translate screen coordinates into world units
            camera.unproject(point.set(screenX, screenY, 0));

            light.setPosition(point.x, point.y);
            light.setActive(true);

            return true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            light.setActive(false);

            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        camera.unproject(point.set(x, y, 0));
        if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
            light.setPosition(point.x, point.y);
        }
        return false;
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

        //bodyItem.setTransform(bodyItem.getPosition().x + translate.x, bodyItem.getPosition().y + translate.y, (float)angle);
        bodyItem.setLinearVelocity(translate);
        bodyItem.setTransform(bodyItem.getPosition().x, bodyItem.getPosition().y, (float)angle);
        item.setPosition(bodyItem.getPosition().x - 16 * MyMap.UNIT_SCALE / 2, bodyItem.getPosition().y - 16 * MyMap.UNIT_SCALE / 2);
        lightItem.setPosition(bodyItem.getPosition().x, bodyItem.getPosition().y);

        //camera.translate(translate);
        camera.position.set(bodyItem.getPosition().x, bodyItem.getPosition().y, 0);

        float elapsedTime = TimeUtils.timeSinceMillis(startTime) / 1000f;
        //loop.setPosition(5 + 3 * MathUtils.cos(elapsedTime), 8 + 2 * MathUtils.sin(elapsedTime));

        // @todo modifier la durée pour ne pas clignoter
        //lightItem.setColor(127f, 127f, 127f, MathUtils.random(0f, 1f));

        camera.update();
        world.step(Gdx.graphics.getDeltaTime(), 8, 3);

        if (world.getContactCount() > 0) {
            if (TimeUtils.timeSinceMillis(lastTime) > 1000) {
                lastTime=TimeUtils.millis();
                //Gdx.app.debug(TAG, lastTime + " -- " + world.getContactCount());
            }
        }
    }

    @Override
    public void render() {
        update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _mapRenderer.setView(camera);
        _mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(item, item.getX(), item.getY(), item.getWidth(), item.getHeight());
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

    private void keyPressed(int keycode, int keyDown) {
        switch (keycode) {
            case Input.Keys.LEFT:
                translate.x = -velocity*keyDown;
                angle=Math.PI;
                break;
            case Input.Keys.RIGHT:
                //Gdx.app.debug(TAG, "RIGHT direction");
                translate.x = velocity*keyDown;
                angle=0f;
                break;
            case Input.Keys.UP:
                translate.y = velocity*keyDown;
                angle=Math.PI/2;
                break;
            case Input.Keys.DOWN:
                translate.y = -velocity*keyDown;
                angle=3*Math.PI/2;
                break;
            case Input.Keys.SPACE:
                addLight();
                break;
            default:
        }

    }

    @Override
    public boolean keyUp(int keycode) {
        keyPressed(keycode, 0);
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyDown(int keycode) {
        keyPressed(keycode, 1);
        //bodyItem.setLinearVelocity(new Vector2(0, 0));

        return super.keyDown(keycode);
    }

    private void addLight() {
        Light tmp = new PointLight(rayHandler, 16);
        tmp.setColor(Color.DARK_GRAY);
        tmp.setDistance(6f);
        tmp.setActive(true);
        tmp.setPosition(camera.position.x, camera.position.y);
        Gdx.app.debug(TAG, "****************************************");
    }

    public void testMap() {
        _mapMgr = new MyMap();
        _mapRenderer = new OrthogonalTiledMapRenderer(_mapMgr.getCurrentMap(), MyMap.UNIT_SCALE);
        _mapRenderer.setView(camera);
        createBox2DMapLayer();
    }

    private void setupViewport(int width, int height) {
        //make the viewport a percentage of the total display area
        VIEWPORT.virtualWidth = width;
        VIEWPORT.virtualHeight = height;

        // current viewport dimensions
        VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
        VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;

        // pixel dimensions of display
        VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
        VIEWPORT.physicalHeight = Gdx.graphics.getHeight();

        // aspect ratio for current viewport
        VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth / VIEWPORT.virtualHeight);

        if (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio) {
            VIEWPORT.viewportWidth = VIEWPORT.viewportHeight * (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight);
            VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        } else {
            VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
            VIEWPORT.viewportHeight = VIEWPORT.viewportWidth * (VIEWPORT.physicalHeight / VIEWPORT.physicalWidth);
        }

        /*
        Gdx.app.debug(TAG, "WorldRenderer virtual  " + VIEWPORT.virtualWidth + "/" + VIEWPORT.virtualHeight);
        Gdx.app.debug(TAG, "WorldRenderer viewport " + VIEWPORT.viewportWidth + "/" + VIEWPORT.viewportHeight);
        Gdx.app.debug(TAG, "WorldRenderer physical " + VIEWPORT.physicalWidth + "/" + VIEWPORT.physicalHeight);
         */
    }


    private boolean createBox2DMapLayer() {
        MapLayer mapCollisionLayer = _mapMgr.getCollisionLayer();

        if (mapCollisionLayer == null) {
            return false;
        }

        for (MapObject object : mapCollisionLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                createBoxFromRectangleMap(((RectangleMapObject) object).getRectangle());
            } else {
                MapProperties mp = ((PolygonMapObject) object).getProperties();
                getPolygon(((PolygonMapObject) object).getPolygon(), Float.parseFloat(mp.get("x").toString()), Float.parseFloat(mp.get("y").toString()));
            }
        }

        return false;
    }

    private void getPolygon(Polygon polygon, float x, float y) {
        float[] tmp = polygon.getVertices();
        Vector2[] vertices = new Vector2[tmp.length / 2];

        for (int i = 0; i < tmp.length; i += 2) {
            vertices[i / 2] = new Vector2((int) (tmp[i] * MyMap.UNIT_SCALE), (int) (tmp[i + 1] * MyMap.UNIT_SCALE));
        }

        BodyDef staticBodyDef = new BodyDef();
        staticBodyDef.type = BodyType.StaticBody;
        Body boxBody0 = world.createBody(staticBodyDef);

        ChainShape shape = new ChainShape();
        shape.createLoop(vertices);
        boxBody0.createFixture(shape, 1f);
        shape.dispose();
        boxBody0.setTransform(new Vector2(x * MyMap.UNIT_SCALE, y * MyMap.UNIT_SCALE), 0f);
        boxBody0.setUserData("Polygon");
    }

    private void addItem() {
        Texture texture = new Texture(Gdx.files.internal("item.png"));
        item = new Sprite(texture);
        item.setSize(item.getWidth() * MyMap.UNIT_SCALE, item.getHeight() * MyMap.UNIT_SCALE);

        PolygonShape boxItem = new PolygonShape();
        boxItem.setAsBox(16 * MyMap.UNIT_SCALE / 2, 16 * MyMap.UNIT_SCALE / 2);

        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.position.set(VIEWPORT.viewportWidth / 2f, VIEWPORT.viewportHeight / 2);
        //camera.position.set(VIEWPORT.viewportWidth / 2f, VIEWPORT.viewportHeight / 2, 0);
        //boxBodyDef.type = BodyType.KinematicBody;
        boxBodyDef.type=BodyType.DynamicBody;

        bodyItem = world.createBody(boxBodyDef);
        FixtureDef boxFixtureDef = new FixtureDef();
        boxFixtureDef.shape = boxItem;
        boxFixtureDef.restitution = 0f;
        boxFixtureDef.density = 2.0f;
        bodyItem.createFixture(boxFixtureDef);
        bodyItem.setUserData(item);

        lightItem = new ConeLight(rayHandler, 12, Color.GRAY, 5, 15, 5, 270, 30);
        lightItem.setSoft(false);
        lightItem.attachToBody(bodyItem);

        item.setPosition(bodyItem.getPosition().x - 16 * MyMap.UNIT_SCALE / 2, bodyItem.getPosition().y - 16 * MyMap.UNIT_SCALE / 2);
        item.setBounds(item.getX(), item.getY(), item.getWidth(), item.getHeight());
    }


}