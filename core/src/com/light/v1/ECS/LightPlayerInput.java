package com.light.v1.ECS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

public class LightPlayerInput extends LightInput implements InputProcessor {
    private static final String TAG = "LightPlayerInput";

    public LightPlayerInput() {
        Gdx.input.setInputProcessor(this);
    }


    @Override
    public void update(LightEntity lightEntity, Batch batch) {
        LightPlayerEntity lightPlayerEntity=(LightPlayerEntity)lightEntity;
        updateKeys(lightPlayerEntity);
        updateButtons(lightPlayerEntity);
    }

    private void updateKeys(LightPlayerEntity lightPlayerEntity) {
        for (Map.Entry<ECSEventInput.Keys, ECSEventInput.States> entry : keys.entrySet()) {
            if (entry.getValue() != ECSEventInput.States.IDLE) {
                lightPlayerEntity.sendMessage(ECSEvent.EVENT.CURRENT_DIRECTION, json.toJson(entry.getKey()+ECSEvent.MESSAGE_TOKEN+entry.getValue()));
            }

            if (entry.getValue() == ECSEventInput.States.DOWN) {
                keys.put(entry.getKey(), ECSEventInput.States.PRESSED);
            }

            if (entry.getValue() == ECSEventInput.States.UP) {
                keys.put(entry.getKey(), ECSEventInput.States.IDLE);
            }
        }
    }

    private void updateButtons(LightPlayerEntity lightPlayerEntity) {
        for (Map.Entry<ECSEventInput.Buttons, ECSEventInput.States> entry : buttons.entrySet()) {
            if (entry.getValue() != ECSEventInput.States.IDLE) {
                lightPlayerEntity.sendMessage(ECSEvent.EVENT.CURRENT_ACTION, json.toJson(entry.getKey()+ECSEvent.MESSAGE_TOKEN+entry.getValue()+ECSEvent.MESSAGE_TOKEN+mousePosition.x+ECSEvent.MESSAGE_TOKEN+mousePosition.y));
            }

            if (entry.getValue() == ECSEventInput.States.DOWN) {
                buttons.put(entry.getKey(), ECSEventInput.States.PRESSED);
            }

            if (entry.getValue() == ECSEventInput.States.UP) {
                buttons.put(entry.getKey(), ECSEventInput.States.IDLE);
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
        keyPressed(keycode, ECSEventInput.States.UP);
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        keyPressed(keycode, ECSEventInput.States.DOWN);
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
            buttons.put(ECSEventInput.Buttons.RIGHT, ECSEventInput.States.DOWN);
            return true;
        }
        else if (button == Input.Buttons.LEFT) {
            buttons.put(ECSEventInput.Buttons.LEFT, ECSEventInput.States.DOWN);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        mousePosition=new Vector2(screenX, screenY);

        if (button == Input.Buttons.RIGHT) {
            buttons.put(ECSEventInput.Buttons.RIGHT, ECSEventInput.States.UP);
            return true;
        }
        else if (button == Input.Buttons.LEFT) {
            buttons.put(ECSEventInput.Buttons.LEFT, ECSEventInput.States.UP);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        mousePosition=new Vector2(x, y);
        return true;
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
