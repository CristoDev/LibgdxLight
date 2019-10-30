package com.light.v1;

import com.badlogic.gdx.Input;

import java.util.HashMap;
import java.util.Map;

public abstract class LightInput implements Component {
    protected enum Keys {
        LEFT, RIGHT, UP, DOWN,
        QUIT, PAUSE
    }

    protected enum Buttons {
        RIGHT_CLICK, LEFT_CLICK
    }

    protected static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();

    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.UP, false);
        keys.put(Keys.DOWN, false);
        keys.put(Keys.QUIT, false);
        keys.put(Keys.PAUSE, false);
    };

    protected static Map<Buttons, Boolean> buttons = new HashMap<Buttons, Boolean>();

    static {
        buttons.put(Buttons.LEFT_CLICK, false);
        buttons.put(Buttons.RIGHT_CLICK, false);
    };

    protected void keyPressed(int keycode, boolean pressed) {
        switch (keycode) {
            case Input.Keys.LEFT:
                keys.put(Keys.LEFT, pressed);
                break;
            case Input.Keys.RIGHT:
                keys.put(Keys.RIGHT, pressed);
                break;
            case Input.Keys.UP:
                keys.put(Keys.UP, pressed);
                break;
            case Input.Keys.DOWN:
                keys.put(Keys.DOWN, pressed);
                break;
        }
    }
}
