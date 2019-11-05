package com.light.v1;

public class ECSEventInput implements ECSEvent {
    public enum Keys {
        LEFT, RIGHT, UP, DOWN,
        QUIT, PAUSE
    }

    public enum Buttons {
        LEFT,
        RIGHT
    };

    public enum States {
        DOWN,
        PRESSED,
        UP,
        IDLE
    };
}
