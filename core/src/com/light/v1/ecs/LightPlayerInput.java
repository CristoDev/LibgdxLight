package com.light.v1.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class LightPlayerInput extends LightInput implements InputProcessor {
    private static final String TAG = "LightPlayerInput";
    private LightPlayerEntity player;

    public LightPlayerInput(LightPlayerEntity entity) {
        player=entity;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void update(Batch batch) {
        updateKeyDirections(player);
        updateKeyActions(player);
        updateButtons(player);
    }

    @Override
    public void render(Batch batch) {
        // render
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "dispose");
    }

    @Override
    public void receiveMessage(ECSEvent.EVENT event, String message) {
        // receive message
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
            mouseButtons.put(ECSEventInput.Buttons.RIGHT, ECSEventInput.States.DOWN);
            return true;
        }
        else if (button == Input.Buttons.LEFT) {
            mouseButtons.put(ECSEventInput.Buttons.LEFT, ECSEventInput.States.DOWN);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        mousePosition=new Vector2(screenX, screenY);

        if (button == Input.Buttons.RIGHT) {
            mouseButtons.put(ECSEventInput.Buttons.RIGHT, ECSEventInput.States.UP);
            return true;
        }
        else if (button == Input.Buttons.LEFT) {
            mouseButtons.put(ECSEventInput.Buttons.LEFT, ECSEventInput.States.UP);
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