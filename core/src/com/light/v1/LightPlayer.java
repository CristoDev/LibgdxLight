package com.light.v1;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.TimeUtils;
import tools.MyMap;

public class LightPlayer implements InputProcessor {
    private static final String TAG = "LightPlayer";
    private Vector2 translate = new Vector2(0, 0);
    private Vector3 point = new Vector3();
    private double angle=Math.PI/2;
    private float velocity=3f;
    private Light light;
    private Light loop;
    private Sprite item = null;
    private Body bodyItem = null, bodySword=null;
    private Light lightItem=null;
    private float candleAlpha=0.8f;
    private float maxFPS=60f;
    private float itemWidth=16, swordWidth=10;
    private double itemDiag;
    private long startTime;
    private RayHandler rayHandler;
    private OrthographicCamera camera;
    private World world;

    public LightPlayer(RayHandler _rayHandler, OrthographicCamera _camera, World _world) {
        Gdx.input.setInputProcessor(this);
        itemDiag=Math.sqrt(Math.pow(itemWidth/2d, 2)*2);
        Gdx.app.debug(TAG, "diag="+itemDiag);
        startTime = TimeUtils.millis();
        rayHandler=_rayHandler;
        camera=_camera;
        world=_world;
        addItem();
        createSword();
    }

    public void createLights() {
        light = new PointLight(rayHandler, 32);
        light.setActive(false);
        light.setColor(Color.PURPLE);
        light.setDistance(5f);
        loop = new PointLight(rayHandler, 16, Color.YELLOW, 1f, 5, 5);
        loop.setActive(true);
        Light conelight = new ConeLight(rayHandler, 32, Color.WHITE, 20, 5, 5, 270, 45);
        conelight.setSoft(false);
    }

    private void addLight() {
        Light tmp = new PointLight(rayHandler, 16);
        tmp.setColor(Color.DARK_GRAY);
        tmp.setDistance(6f);
        tmp.setActive(true);
        tmp.setPosition(camera.position.x, camera.position.y);
        Gdx.app.debug(TAG, "****************************************");
    }

    private void addItem() {
        Texture texture = new Texture(Gdx.files.internal("item.png"));
        // @TODO creer une classe pour ajouter un nom au sprite et l'utiliser dans bodyItem.setUserData
        item = new Sprite(texture);
        item.setSize(item.getWidth() * MyMap.UNIT_SCALE, item.getHeight() * MyMap.UNIT_SCALE);

        PolygonShape boxItem = new PolygonShape();
        boxItem.setAsBox(itemWidth*0.8f * MyMap.UNIT_SCALE / 2, itemWidth*0.8f * MyMap.UNIT_SCALE / 2);
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.position.set(Box2DLightsSample.VIEWPORT.viewportWidth / 2f, Box2DLightsSample.VIEWPORT.viewportHeight / 2);
        boxBodyDef.type= BodyDef.BodyType.DynamicBody;

        bodyItem = world.createBody(boxBodyDef);
        FixtureDef boxFixtureDef = new FixtureDef();
        boxFixtureDef.shape = boxItem;
        boxFixtureDef.restitution = 0f;
        boxFixtureDef.density = 0f;
        bodyItem.createFixture(boxFixtureDef);
        bodyItem.setUserData(item);

        lightItem = new ConeLight(rayHandler, 12, Color.GRAY, 5, 15, 5, 270, 30);
        lightItem.setSoft(false);
        lightItem.attachToBody(bodyItem);

        item.setPosition(bodyItem.getPosition().x - itemWidth * MyMap.UNIT_SCALE / 2, bodyItem.getPosition().y - itemWidth * MyMap.UNIT_SCALE / 2);
        item.setBounds(item.getX(), item.getY(), item.getWidth(), item.getHeight());
    }

    private void createSword() {
        float length=((float)itemDiag+swordWidth)*MyMap.UNIT_SCALE;

        Vector2[] swordAttack=new Vector2[]{
                new Vector2(0.5f, -0.5f), //A
                new Vector2(0.5f, 0.5f), // B
                new Vector2(length*(float)Math.cos(Math.PI/4), length*(float)Math.sin(Math.PI/4)), //G
                new Vector2(length*(float)Math.cos(Math.PI/8), length*(float)Math.sin(Math.PI/8)), //K
                new Vector2(length, 0), //F
                new Vector2(length*(float)Math.cos(-Math.PI/8), length*(float)Math.sin(-Math.PI/8)), //M
                new Vector2(length*(float)Math.cos(-Math.PI/4), length*(float)Math.sin(-Math.PI/4)), //H
        };

        BodyDef swordBodyDef = new BodyDef();
        swordBodyDef.type= BodyDef.BodyType.DynamicBody; // en attendant mieux -> utilisation des masques
        bodySword = world.createBody(swordBodyDef);
        PolygonShape swordShape=new PolygonShape();
        swordShape.set(swordAttack);
//        bodySword.createFixture(swordShape, 0f);
        FixtureDef swordFixtureDef = new FixtureDef();
        swordFixtureDef.shape = swordShape;
        swordFixtureDef.restitution = 0f;
        swordFixtureDef.density = 0.0f;
        swordFixtureDef.isSensor=true;
        bodySword.createFixture(swordFixtureDef);
        bodySword.setUserData("coup de guiche-guiche");
        bodySword.setActive(false);
        swordShape.dispose();
    }

    private void keyPressed(int keycode, int keyDown) {
        double currentAngle=angle;
        switch (keycode) {
            case Input.Keys.LEFT:
                translate.x = -velocity*keyDown;
                angle=Math.PI;
                break;
            case Input.Keys.RIGHT:
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

        if (keyDown == 0) {
            angle=currentAngle;
        }
    }

    public void update() {
        int fps = Gdx.graphics.getFramesPerSecond();

        if (fps > 0 && fps < maxFPS) {
            float ratio=maxFPS / Gdx.graphics.getFramesPerSecond();
            bodyItem.setLinearVelocity(translate.x*ratio, translate.y*ratio);
            //Gdx.app.debug(TAG, "FPS: "+fps+ "-- > "+ratio);
        }
        else {
            bodyItem.setLinearVelocity(translate);
        }

        bodyItem.setTransform(bodyItem.getPosition().x, bodyItem.getPosition().y, (float)angle);
        item.setPosition(bodyItem.getPosition().x - 16 * MyMap.UNIT_SCALE / 2, bodyItem.getPosition().y - 16 * MyMap.UNIT_SCALE / 2);
        lightItem.setPosition(bodyItem.getPosition().x, bodyItem.getPosition().y);

        float elapsedTime = TimeUtils.timeSinceMillis(startTime) / 1000f;
        loop.setPosition(5 + 3 * MathUtils.cos(elapsedTime), 8 + 2 * MathUtils.sin(elapsedTime));

        // petit effet de bougie sur le cone de lumière
        candleAlpha=MathUtils.clamp(candleAlpha+MathUtils.random(-0.05f, 0.05f), 0.7f, 1f);
        lightItem.setColor(127f, 127f, 127f, candleAlpha);
    }

    @Override
    public boolean keyUp(int keycode) {
        keyPressed(keycode, 0);
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        keyPressed(keycode, 1);

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.RIGHT) {
            //Translate screen coordinates into world units
            camera.unproject(point.set(screenX, screenY, 0));
            light.setPosition(point.x, point.y);
            light.setActive(true);

            return true;
        }

        if (button == Input.Buttons.LEFT) {
            bodySword.setTransform(bodyItem.getPosition().x+(float)Math.cos(bodyItem.getAngle())* MyMap.UNIT_SCALE, bodyItem.getPosition().y+(float)Math.sin(bodyItem.getAngle())*MyMap.UNIT_SCALE, bodyItem.getAngle());
            bodySword.setLinearDamping(5f);
            bodySword.setAngularVelocity(0.01f);
            bodySword.setActive(true);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.RIGHT) {
            light.setActive(false);

            return true;
        }
        if (button == Input.Buttons.LEFT) {
            bodySword.setActive(false);
            Gdx.app.debug(TAG, "set active false");
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        camera.unproject(point.set(x, y, 0));
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            light.setPosition(point.x, point.y);
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public Vector2 getPosition() {
        return bodyItem.getPosition();
    }

    public void render(Batch batch) {
        item.draw(batch);
    }
}
