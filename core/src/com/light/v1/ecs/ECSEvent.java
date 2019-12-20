package com.light.v1.ecs;

public interface ECSEvent {
    String MESSAGE_TOKEN = ":::::";

    enum Event {
        CURRENT_POSITION,
        INIT_POSITION,
        SET_POSITION,
        INIT_COMPONENT,
        CURRENT_DIRECTION,
        SET_STATE,
        COLLISION_WITH_MAP,
        COLLISION_WITH_ENTITY,
        LOAD_ANIMATIONS,
        INIT_DIRECTION,
        INIT_STATE,
        INIT_SELECT_ENTITY,
        ENTITY_SELECTED,
        ENTITY_DESELECTED,
        CURRENT_ACTION,
        CHANGE_POSITION,
        MOUSE_ACTION,
        KEY_ACTION,
        KEY_DIRECTION,
        SPEED_MODIFIER,
        SPEED_MODIFIER_REVERSE,
        PLAY_SOUND,
        ANIMATION
    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    enum Compas {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    // sauf le dernier (IDLE), les autres doivent exister dans AnimationManager.AnimationState
    enum EntityState {
        SPELLCAST,
        THRUST,
        WALK,
        SLASH,
        SHOOT,
        HURT,
        IDLE
    }
}
