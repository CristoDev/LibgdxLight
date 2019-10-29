package com.light.v1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.Map;

public class LightPlayerInput extends LightInput implements InputProcessor {
    private static final String TAG = "LightInput";

    public LightPlayerInput() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void update(LightPlayer lightPlayer, float delta, Batch batch) {
        for (Map.Entry<Keys, Boolean> entry : keys.entrySet()) {
            if (entry.getValue()) {
                // @todo indiquer la direction
                lightPlayer.sendMessage("key", json.toJson(entry.getKey()+MESSAGE_TOKEN+1));
            }
        }

        /*
        if (keyCode != -1) {
            // @TODO mettre en place une table/file d'attente pour gérer plusieurs touches
            if (keyDown == 0) {
                keyCode=-1;
            }
        }
         */
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "dispose");
    }

    @Override
    public void receiveMessage(String event, String message) {
        String[] string = message.split(MESSAGE_TOKEN);
        //Gdx.app.debug(TAG, "Message reçu : "+event+" // "+message);

    }

    @Override
    public boolean keyUp(int keycode) {
        keyPressed(keycode, false);

        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        keyPressed(keycode, true);

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
            //camera.unproject(point.set(screenX, screenY, 0));
            //elementLight.activate(point);
            Gdx.app.debug(TAG, "elementLight.activate(point); + camera");

            return true;
        }

        if (button == Input.Buttons.LEFT) {
            //lightGraphics.updateSword();
            Gdx.app.debug(TAG, "lightGraphics.updateSword();");
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.RIGHT) {
            //elementLight.setActive(false);
            Gdx.app.debug(TAG, "elementLight.setActive(false);");
            return true;
        }
        if (button == Input.Buttons.LEFT) {
            //lightGraphics.setActiveSword(false);
            Gdx.app.debug(TAG, "lightGraphics.setActiveSword(false);");
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
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