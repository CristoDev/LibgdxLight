package com.light.v1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

public class LightPlayerInput extends LightInput implements InputProcessor {
    private static final String TAG = "LightInput";

    public LightPlayerInput() {
        Gdx.input.setInputProcessor(this);
    }


    @Override
    public void update(LightPlayer lightPlayer, float delta, Batch batch) {
        updateKeys(lightPlayer, delta, batch);
        updateButtons(lightPlayer, delta, batch);
    }

    private void updateKeys(LightPlayer lightPlayer, float delta, Batch batch) {
        for (Map.Entry<ECSEventInput.Keys, ECSEventInput.KeyState> entry : keys.entrySet()) {
            if (entry.getValue() != ECSEventInput.KeyState.KEY_IDLE) {
                lightPlayer.sendMessage(ECSEvent.EVENT.CURRENT_DIRECTION, json.toJson(entry.getKey()+ECSEvent.MESSAGE_TOKEN+entry.getValue()));
            }

            // si on lache la touche, on a envoyé le message juste avant on change l'état à IDLE pour ne plus rien envoyer
            if (entry.getValue() == ECSEventInput.KeyState.KEY_RELEASED) {
                keys.put(entry.getKey(), ECSEventInput.KeyState.KEY_IDLE);
            }
        }
    }

    private void updateButtons(LightPlayer lightPlayer, float delta, Batch batch) {
        for (Map.Entry<ECSEventInput.Button, ECSEventInput.ButtonState> entry : buttons.entrySet()) {
            if (entry.getValue() != ECSEventInput.ButtonState.BUTTON_IDLE) {
                lightPlayer.sendMessage(ECSEvent.EVENT.CURRENT_ACTION, json.toJson(entry.getKey()+ECSEvent.MESSAGE_TOKEN+entry.getValue()+ECSEvent.MESSAGE_TOKEN+mousePosition.x+ECSEvent.MESSAGE_TOKEN+mousePosition.y));
            }

            // si on lache le bouton, on a envoyé le message juste avant on change l'état à IDLE pour ne plus rien envoyer
            if (entry.getValue() == ECSEventInput.ButtonState.BUTTON_RELEASED) {
                buttons.put(entry.getKey(), ECSEventInput.ButtonState.BUTTON_IDLE);
            }
        }
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "dispose");
    }

    @Override
    public void receiveMessage(ECSEvent.EVENT event, String message) {
    }

    @Override
    public boolean keyUp(int keycode) {
        keyPressed(keycode, ECSEventInput.KeyState.KEY_RELEASED);

        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        keyPressed(keycode, ECSEventInput.KeyState.KEY_PRESSED);

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        mousePosition=new Vector2(screenX, screenY);

        if (button == Input.Buttons.RIGHT) {
            buttons.put(ECSEventInput.Button.RIGHT, ECSEventInput.ButtonState.BUTTON_CLICK);
            Gdx.app.debug(TAG, "TODO - elementLight.activate(point); + camera");

            return true;
        }

        if (button == Input.Buttons.LEFT) {
            buttons.put(ECSEventInput.Button.LEFT, ECSEventInput.ButtonState.BUTTON_CLICK);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        mousePosition=new Vector2(screenX, screenY);

        if (button == Input.Buttons.RIGHT) {
            buttons.put(ECSEventInput.Button.RIGHT, ECSEventInput.ButtonState.BUTTON_RELEASED);
            //elementLight.setActive(false);
            Gdx.app.debug(TAG, "TODO - elementLight.setActive(false);");
            return true;
        }
        if (button == Input.Buttons.LEFT) {
            buttons.put(ECSEventInput.Button.LEFT, ECSEventInput.ButtonState.BUTTON_RELEASED);
            //lightGraphics.setActiveSword(false);
            Gdx.app.debug(TAG, "TODO - lightGraphics.setActiveSword(false);");
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        Gdx.app.debug(TAG, "dragged");

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
