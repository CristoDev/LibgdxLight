package com.light.v1;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.light.v1.element.WorldLightTest;
import com.light.v1.element.WorldTorch;

public class LightPlayer implements InputProcessor, Observer {
    private static final String TAG = "LightPlayer";

    private Vector3 point = new Vector3();

    private float velocity=3f;



    private RayHandler rayHandler;
    private OrthographicCamera camera;
    private World world;
    private WorldLightTest elementLight;

    private LightInput lightInput;
    private LightPlayerGraphics lightGraphics;

    public LightPlayer(RayHandler _rayHandler, OrthographicCamera _camera, World _world) {
        // @todo modifier le code pour utiliser lightInput
        Gdx.input.setInputProcessor(this);

        rayHandler=_rayHandler;
        camera=_camera;
        world=_world;
        lightInput=new LightInput();
        lightGraphics=new LightPlayerGraphics();
        lightGraphics.addItem(world, rayHandler);
        lightGraphics.createSword(world);
    }

    public void createLights() {
        elementLight=new WorldLightTest(rayHandler);
    }

    private void addLight() {
        new WorldTorch(rayHandler, camera.position);
    }

    private void keyPressed(int keycode, int keyDown) {
        double currentAngle=lightGraphics.getAngle();
        double angle=currentAngle;
        Vector2 translate=new Vector2(0, 0);

        switch (keycode) {
            case Input.Keys.LEFT:
                translate.x = -velocity * keyDown;
                angle = Math.PI;
                break;
            case Input.Keys.RIGHT:
                translate.x = velocity * keyDown;
                angle = 0f;
                break;
            default:
        }

        switch (keycode) {
            case Input.Keys.UP:
                translate.y = velocity*keyDown;
                angle = Math.PI / 2;
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

        lightGraphics.setAngle(angle);
        lightGraphics.setTranslate(translate);
    }

    public void update() {
        lightInput.update(this, 0);
        lightGraphics.update(this, 0);

        elementLight.update();
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
            elementLight.activate(point);

            return true;
        }

        if (button == Input.Buttons.LEFT) {
            lightGraphics.updateSword();
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.RIGHT) {
            elementLight.setActive(false);
            return true;
        }
        if (button == Input.Buttons.LEFT) {
            lightGraphics.setActiveSword(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        camera.unproject(point.set(x, y, 0));
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            elementLight.setPosition(point);
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
        return lightGraphics.getPosition();
    }

    public void render(Batch batch) {
        lightGraphics.render(batch);
    }

    @Override
    public void onNotify(ObserverNotifier.Event event, String message) {
        Gdx.app.debug(TAG, "onNotify -> LightPlayer");
    }

    public void sendMessage(String event, String message) {
        lightInput.receiveMessage(event, message);
        lightGraphics.receiveMessage(event, message);
    }
}
