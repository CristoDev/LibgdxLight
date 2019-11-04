package com.light.v1;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

public abstract class LightInput implements Component {
    protected static Map<ECSEventInput.Keys, ECSEventInput.KeyState> keys = new HashMap<ECSEventInput.Keys, ECSEventInput.KeyState>();

    static {
        keys.put(ECSEventInput.Keys.LEFT, ECSEventInput.KeyState.KEY_IDLE);
        keys.put(ECSEventInput.Keys.RIGHT, ECSEventInput.KeyState.KEY_IDLE);
        keys.put(ECSEventInput.Keys.UP, ECSEventInput.KeyState.KEY_IDLE);
        keys.put(ECSEventInput.Keys.DOWN, ECSEventInput.KeyState.KEY_IDLE);
        keys.put(ECSEventInput.Keys.QUIT, ECSEventInput.KeyState.KEY_IDLE);
        keys.put(ECSEventInput.Keys.PAUSE, ECSEventInput.KeyState.KEY_IDLE);
    };

    protected static Map<ECSEventInput.Button, ECSEventInput.ButtonState> buttons = new HashMap<ECSEventInput.Button, ECSEventInput.ButtonState>();

    static {
        buttons.put(ECSEventInput.Button.LEFT, ECSEventInput.ButtonState.BUTTON_IDLE);
        buttons.put(ECSEventInput.Button.RIGHT, ECSEventInput.ButtonState.BUTTON_IDLE);
    };

    protected void keyPressed(int keycode, ECSEventInput.KeyState state) {
        switch (keycode) {
            case Input.Keys.LEFT:
                keys.put(ECSEventInput.Keys.LEFT, state);
                break;
            case Input.Keys.RIGHT:
                keys.put(ECSEventInput.Keys.RIGHT, state);
                break;
            case Input.Keys.UP:
                keys.put(ECSEventInput.Keys.UP, state);
                break;
            case Input.Keys.DOWN:
                keys.put(ECSEventInput.Keys.DOWN, state);
                break;
        }
    }

    protected static Vector2 mousePosition=new Vector2(0, 0);
}
