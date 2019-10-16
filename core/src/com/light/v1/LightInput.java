package com.light.v1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class LightInput extends ObserverNotifier implements InputProcessor {
    private static final String TAG = "LightInput";
    private boolean keyDown=false;
    private int keyCode=-1;

    public LightInput() {
        //Gdx.input.setInputProcessor(this);
    }

    public void update(LightPlayer lightPlayer, float delta) {
        if (keyCode != -1) {
            lightPlayer.sendMessage("key", "code: " + keyCode + " etat: " + keyDown);
        }
    }

    public void receiveMessage(String event, String message) {
        Gdx.app.debug(TAG, "Message reçu: "+event+" // "+message);

    }


    @Override
    public boolean keyDown(int keycode) {
        keyDown=true;
        keyCode=keycode;

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keyDown=false;
        keyCode=keycode;

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
}
