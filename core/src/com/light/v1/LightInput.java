package com.light.v1;

import com.badlogic.gdx.Input;

import java.util.HashMap;
import java.util.Map;

public abstract class LightInput implements Component {
    protected int keyDown=0;
    protected int keyCode=-1;

    protected enum Keys {
        LEFT, RIGHT, UP, DOWN, QUIT, PAUSE
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

    protected void keyPressed(int keycode, boolean pressed) {
        switch (keycode) {
            case Input.Keys.LEFT:
                keys.put(Keys.LEFT, pressed);
                break;
            case Input.Keys.RIGHT:
                keys.put(Keys.RIGHT, pressed);
                break;
        }

        switch (keycode) {
            case Input.Keys.UP:
                keys.put(Keys.UP, pressed);
                break;
            case Input.Keys.DOWN:
                keys.put(Keys.DOWN, pressed);
                break;
        }
    }
}
