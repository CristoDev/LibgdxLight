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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.TimeUtils;

public class Box2DLightsSample extends InputAdapter implements ApplicationListener {
    private static final String TAG = "Box2DLightsSample";

    //private static final float SCENE_WIDTH = 12.80f; // 12.8 metres wide
    //private static final float SCENE_HEIGHT = 7.20f; // 7.2 metres high

    //private Viewport viewport;
    private Vector3 point = new Vector3();
    private SpriteBatch batch;
    private OrthographicCamera camera=null;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private RayHandler rayHandler;
    private Light light, loop;
    private long startTime;

    ShapeRenderer sr;


    private static class VIEWPORT {
        static float viewportWidth;
        static float viewportHeight;
        static float virtualWidth;
        static float virtualHeight;
        static float physicalWidth;
        static float physicalHeight;
        static float aspectRatio;
    }


    private OrthogonalTiledMapRenderer _mapRenderer=null;
    private static MyMap _mapMgr;

    private Contact contact=null;

    @Override
    public void create () {
        setupViewport(20, 20);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
        camera.position.set(VIEWPORT.viewportWidth/2f, VIEWPORT.viewportHeight/2, 0);
        camera.update();


        //viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT);
        // Center camera
        /*
        viewport.getCamera().position.set(viewport.getCamera().position.x + SCENE_WIDTH*0.5f,
                viewport.getCamera().position.y + SCENE_HEIGHT*0.5f
                , 0);
        viewport.getCamera().update();
*/

        batch = new SpriteBatch();

        Gdx.input.setInputProcessor(this);

        // Create Physics World
        world = new World(new Vector2(0,-9.8f), true);
        // Instantiate the class in charge of drawing physics shapes
        debugRenderer = new Box2DDebugRenderer();
        // To add some color to the ground
        sr = new ShapeRenderer();

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.2f, 0.2f, 0.2f, 0.25f);
        light = new PointLight(rayHandler, 32);
        light.setActive(false);
        light.setColor(Color.PURPLE);
        light.setDistance(5f);

        loop=new PointLight(rayHandler, 16, Color.YELLOW, 1f, 5, 5);
        loop.setActive(true);

        //loop=new ConeLight(rayHandler, 32, Color.YELLOW, 10, 5, 5, 270, 45);
        //loop.setSoft(false);
        //loop=new PointLight(rayHandler, 32, Color.BLUE, 10, 5, 5);

        createBodies();
        Light conelight = new ConeLight(rayHandler, 32, Color.WHITE, 20, 5, 5, 270, 45);
        conelight.setSoft(false);
        startTime = TimeUtils.millis();

        testMap();
    }

    private void createBodies() {

        // Create a static body definition
        BodyDef staticBodyDef = new BodyDef();
        staticBodyDef.type = BodyType.StaticBody;

        //GROUND
        Body groundBody = world.createBody(staticBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(6, 0.5f);
        groundBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();

        groundBody.setTransform(new Vector2(6, 0.5f), groundBody.getAngle());

        // BOX
        Body boxBody = world.createBody(staticBodyDef);
        PolygonShape box = new PolygonShape();
        box.setAsBox(1f, .5f);
        boxBody.createFixture(box, 0.0f);
        box.dispose();

        // !!! milieu de la box
        boxBody.setTransform(new Vector2(6, 3), 0);


        Body boxBody2 = world.createBody(staticBodyDef);
        PolygonShape box2 = new PolygonShape();
        box2.setAsBox(1f, 1f);
        boxBody2.createFixture(box2, 0.0f);
        box2.dispose();

        // !!! milieu de la box
        boxBody2.setTransform(new Vector2(4, 4), (float) Math.PI/4);

    }

    //private void createBoxFromRectangleMap(RectangleMapObject rectangleMapObject) {
    private void createBoxFromRectangleMap(Rectangle rectangle) {
        BodyDef staticBodyDef = new BodyDef();
        staticBodyDef.type = BodyType.StaticBody;
        //Rectangle rectangle=rectangleMapObject.getRectangle();

        //Gdx.app.debug(TAG, "rectangle "+rectangle.getX()+"/"+rectangle.y+" W/h "+rectangle.getWidth()+"/"+rectangle.getHeight());

        Body boxBody2 = world.createBody(staticBodyDef);
        PolygonShape box2 = new PolygonShape();
        box2.setAsBox(rectangle.getWidth()*MyMap.UNIT_SCALE/2, rectangle.getHeight()*MyMap.UNIT_SCALE/2);
        boxBody2.createFixture(box2, 0.0f);
        box2.dispose();

        // !!! milieu de la box
        boxBody2.setTransform(new Vector2(rectangle.getX()*MyMap.UNIT_SCALE+rectangle.getWidth()*MyMap.UNIT_SCALE/2, rectangle.getY()*MyMap.UNIT_SCALE+rectangle.getHeight()*MyMap.UNIT_SCALE/2), 0);

    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {

            //Translate screen coordinates into world units
            //viewport.getCamera().unproject(point.set(screenX, screenY, 0));
            camera.unproject(point.set(screenX, screenY, 0));

            light.setPosition(point.x, point.y);
            light.setActive(true);

            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {

            light.setActive(false);

            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        //viewport.getCamera().unproject(point.set(x, y, 0));
        camera.unproject(point.set(x, y, 0));
        if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
            light.setPosition(point.x, point.y);
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        //viewport.update(width, height);
    }

    @Override
    public void dispose() {
        debugRenderer.dispose();

        batch.dispose();
        rayHandler.dispose();
        world.dispose();
    }

    private void update() {
        float elapsedTime = TimeUtils.timeSinceMillis(startTime)/1000f;

        loop.setPosition(5+3*MathUtils.cos(elapsedTime), 8+2*MathUtils.sin(elapsedTime));
    }

    @Override
    public void render () {
        update();
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1/60f, 6, 2);
/*
        sr.setProjectionMatrix(viewport.getCamera().combined);
        sr.begin(ShapeType.Filled);
        sr.setColor(Color.RED);
        sr.rect(0, 0, SCENE_WIDTH, 1f);
        sr.end();
*/
        _mapRenderer.setView(camera);
        _mapRenderer.render();

        //rayHandler.setCombinedMatrix(viewport.getCamera().combined);
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

    @Override
    public boolean keyUp(int keycode) {
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyDown(int keycode) {
        Vector2 translate=new Vector2(0, 0);
        switch (keycode) {
            case Input.Keys.LEFT:
                translate.x=-1;
                break;
            case Input.Keys.RIGHT:
                translate.x=1;
                break;
            case Input.Keys.UP:
                translate.y=1;
                break;
            case Input.Keys.DOWN:
                translate.y=-1;
                break;
            case Input.Keys.SPACE:
                addLight();
                break;
            default:
        }

        camera.translate(translate);

        return super.keyDown(keycode);
    }


    private void addLight() {
        Light tmp=new PointLight(rayHandler, 16);
        tmp.setColor(Color.DARK_GRAY);
        tmp.setDistance(6f);
        tmp.setActive(true);
        tmp.setPosition(camera.position.x, camera.position.y);
    }

    public void testMap() {
        _mapMgr=new MyMap();
        _mapRenderer=new OrthogonalTiledMapRenderer(_mapMgr.getCurrentMap(), MyMap.UNIT_SCALE);
        _mapRenderer.setView(camera);
        isCollisionWithMapLayer();
    }

    private void setupViewport(int width, int height) {
        //make the viewport a percentage of the total display area
        VIEWPORT.virtualWidth=width;
        VIEWPORT.virtualHeight=height;

        // current viewport dimensions
        VIEWPORT.viewportWidth=VIEWPORT.virtualWidth;
        VIEWPORT.viewportHeight=VIEWPORT.virtualHeight;

        // pixel dimensions of display
        VIEWPORT.physicalWidth=Gdx.graphics.getWidth();
        VIEWPORT.physicalHeight=Gdx.graphics.getHeight();

        // aspect ratio for current viewport
        VIEWPORT.aspectRatio=(VIEWPORT.virtualWidth/VIEWPORT.virtualHeight);

        if (VIEWPORT.physicalWidth/VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio) {
            VIEWPORT.viewportWidth=VIEWPORT.viewportHeight*(VIEWPORT.physicalWidth/VIEWPORT.physicalHeight);
            VIEWPORT.viewportHeight=VIEWPORT.virtualHeight;
        }
        else {
            VIEWPORT.viewportWidth=VIEWPORT.virtualWidth;
            VIEWPORT.viewportHeight=VIEWPORT.viewportWidth*(VIEWPORT.physicalHeight/VIEWPORT.physicalWidth);
        }

        Gdx.app.debug(TAG, "WorldRenderer virtual  " + VIEWPORT.virtualWidth +"/"+VIEWPORT.virtualHeight);
        Gdx.app.debug(TAG, "WorldRenderer viewport " + VIEWPORT.viewportWidth +"/"+VIEWPORT.viewportHeight);
        Gdx.app.debug(TAG, "WorldRenderer physical " + VIEWPORT.physicalWidth +"/"+VIEWPORT.physicalHeight);
    }


    private boolean isCollisionWithMapLayer() {
        MapLayer mapCollisionLayer=_mapMgr.getCollisionLayer();

        if (mapCollisionLayer == null) {
            return false;
        }

        for (MapObject object: mapCollisionLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                //Gdx.app.debug(TAG, "objet de type RectangleMapObject");
                createBoxFromRectangleMap(((RectangleMapObject) object).getRectangle());
            }
            else {
                Gdx.app.debug(TAG, "**** objet de type "+object.getClass().getSimpleName());
                getPolygon(((PolygonMapObject)object).getPolygon());
            }
        }

        return false;
    }

    private void getPolygon(Polygon polygon) {
        polygon.setScale(MyMap.UNIT_SCALE, MyMap.UNIT_SCALE);
        float[] tmp=polygon.getVertices();
        Vector2[] vertices=new Vector2[tmp.length/2];
        for (int i=0; i<tmp.length; i+=2) {
            vertices[i/2]=new Vector2((int)(tmp[i]*MyMap.UNIT_SCALE), (int)(tmp[i+1]*MyMap.UNIT_SCALE));
        }

        /*
        for (int i=0; i<vertices.length; i++) {
            Gdx.app.debug("", "verticesOK["+i+"] = new Vector2("+vertices[i].x+"f, "+vertices[i].y+"f);");
        }

         */


        /*
        BodyDef staticBodyDef = new BodyDef();
        staticBodyDef.type = BodyType.StaticBody;
        Body boxBody0 = world.createBody(staticBodyDef);
        PolygonShape box0 = new PolygonShape();
        //box0.set(vertices);
        box0.setAsBox(5, 5);
        boxBody0.createFixture(box0, 0.0f);
        box0.dispose();
        // !!! milieu de la box
        boxBody0.setTransform(new Vector2(0, 0), 0);


         */

        /*
        Vector2[] verticesOK = new Vector2[8];

        verticesOK[0] = new Vector2(0f , -0f  );
        verticesOK[1] = new Vector2(0f , -4f  );
        verticesOK[2] = new Vector2(5f , 2f);
        verticesOK[3] = new Vector2(5f , 3f);
        verticesOK[4] = new Vector2(12.375f , -3f);
        verticesOK[5] = new Vector2(12.468f , 3f);
        verticesOK[6] = new Vector2(15.40625f , 4f);
        verticesOK[7] = new Vector2(17.1875f , -0.03125f);
*/
        Vector2[] verticesOK = new Vector2[8];

        verticesOK[0] = new Vector2(0.0f, 0.0f);
        verticesOK[1] = new Vector2(0.0f, -4.0f);
        verticesOK[2] = new Vector2(5.0f, -4.0f);
        verticesOK[3] = new Vector2(5.0f, -6.0f);
        verticesOK[4] = new Vector2(12.0f, -6.0f);
        verticesOK[5] = new Vector2(12.0f, -5.0f);
        verticesOK[6] = new Vector2(15.0f, -5.0f);
        verticesOK[7] = new Vector2(15.0f, -4.0f);

        //verticesOK[8] = new Vector2(17.0f, -4.0f);
        /*
        verticesOK[9] = new Vector2(17.0f, 0.0f);
        */
        BodyDef staticBodyDef = new BodyDef();
        staticBodyDef.type = BodyType.StaticBody;
        Body boxBody0 = world.createBody(staticBodyDef);

        PolygonShape shape = new PolygonShape();
        shape.set(verticesOK);
        //shape.setAsBox(4, 9);
        boxBody0.createFixture(shape, 1f);
        shape.dispose();
        boxBody0.setTransform(new Vector2(10, 15), 0f);

    }

    private void collision() {
        contact=new MyContact(world, 1l);

    }
    /*
    https://www.reddit.com/r/libgdx/comments/2b2a71/tiled_polygon_to_box2d_polygonshape/cj1qxtw/?context=8&depth=9

_polyShape = new PolygonShape();
Polygon _polygon = ((PolygonMapObject) _mapObject).getPolygon();

_bdef.position.set((_polygon.getX() * MAP_SCALE / PPM),
			_polygon.getY() * MAP_SCALE / PPM);

_polygon.setPosition(0, 0);
_polygon.setScale(MAP_SCALE / PPM, MAP_SCALE / PPM);

_polyShape.set(_polygon.getTransformedVertices());
_fdef.shape = _polyShape;

m_world.createBody(_bdef).createFixture(_fdef);

PPM = My Pixels per Meter (100)

MAP_SCALE = The amount I scaled my Tiled map by (0.8f)
     */
}