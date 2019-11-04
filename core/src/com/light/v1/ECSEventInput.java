package com.light.v1;

public class ECSEventInput implements ECSEvent {
    public enum Keys {
        LEFT, RIGHT, UP, DOWN,
        QUIT, PAUSE
    }

    public enum KeyState {
        KEY_PRESSED,
        KEY_RELEASED,
        KEY_IDLE
    };

    public enum Button {
        LEFT,
        RIGHT
    };

    public enum ButtonState {
        BUTTON_CLICK,
        BUTTON_RELEASED,
        BUTTON_IDLE
    };
}
