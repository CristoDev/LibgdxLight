package com.light.v1.ecs;

public class ECSEventInput implements ECSEvent {
    public enum Keys {
        LEFT, RIGHT, UP, DOWN,
        SPACE,
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
