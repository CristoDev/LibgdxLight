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
        SET_DIRECTION,
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

    enum Compas {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    // peut etre utiliser une classe avec une variable reprenant l'enum?
    enum AnimationState {
        SPELLCAST(0, 3, 7),
        THRUST(4, 7, 8),
        WALK(8, 11, 9),
        SLASH(12, 15, 6),
        SHOOT(16,19,13),
        HURT(20,20,6),
        IDLE(8,11,1);

        private int rowStart;
        private int rowEnd;
        private int framesCount;

        AnimationState(int start, int end, int count) {
            rowStart=start;
            rowEnd=end;
            framesCount=count;
        }

        public int getRowStart() {
            return rowStart;
        }

        public int getRowEnd() {
            return rowEnd;
        }

        public int getFramesCount() {
            return framesCount;
        }
    }

    enum AnimationDirection {
        UP(0),
        LEFT(1),
        DOWN(2),
        RIGHT(3);

        private int index;

        AnimationDirection(int i) {
            index=i;
        }

        public int getIndex() {
            return index;
        }
    }
}
