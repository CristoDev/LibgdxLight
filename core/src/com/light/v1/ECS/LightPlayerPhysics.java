package com.light.v1.ECS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

public class LightPlayerPhysics extends LightPhysics {
    private static final String TAG = "LightPlayerPhysics";

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "dispose");
    }

    @Override
    public void receiveMessage(ECSEvent.EVENT event, String message) {
        if (event == ECSEvent.EVENT.CURRENT_DIRECTION) {
            //keyPressed(message);
        }
        else if (event == ECSEvent.EVENT.CURRENT_ACTION) {
            //buttonPressed(message);
        }
    }

    @Override
    public void update(LightEntity lightEntity, Batch batch) {
        LightPlayerEntity lightPlayerEntity=(LightPlayerEntity)lightEntity;
    }
/*
    private void keyPressed(String message) {
        String[] string = message.split(ECSEvent.MESSAGE_TOKEN);
        float velocity=player.getVelocity();
        double angle=getAngle();

        ECSEventInput.Keys direction = json.fromJson(ECSEventInput.Keys.class, string[0]);
        ECSEventInput.States state=json.fromJson(ECSEventInput.States.class, string[1]);

        if (direction == ECSEventInput.Keys.LEFT) {
            translate.x=0;
            if (state == ECSEventInput.States.DOWN || state == ECSEventInput.States.PRESSED) {
                translate.x = -velocity;
                angle = Math.PI + Math.PI/4*Math.signum(translate.y);
            }
        }
        else if (direction == ECSEventInput.Keys.RIGHT) {
            translate.x=0;
            if (state == ECSEventInput.States.DOWN || state == ECSEventInput.States.PRESSED) {
                translate.x = velocity;
                angle = 0f + Math.PI/4*Math.signum(translate.y);
            }
        }
        else if (direction == ECSEventInput.Keys.UP) {
            translate.y=0;
            if (state == ECSEventInput.States.DOWN || state == ECSEventInput.States.PRESSED) {
                translate.y = velocity;
                angle = Math.PI / 2 - Math.PI/4*Math.signum(translate.x);
            }
        }
        else if (direction == ECSEventInput.Keys.DOWN) {
            translate.y=0;
            if (state == ECSEventInput.States.DOWN || state == ECSEventInput.States.PRESSED) {
                translate.y = -velocity;
                angle = 3*Math.PI / 2 + Math.PI/4*Math.signum(translate.x);
            }
        }

        setAngle(angle);
        setTranslate(translate);
    }

    private void buttonPressed(String message) {
        String[] string = message.split(ECSEvent.MESSAGE_TOKEN);
        ECSEventInput.Buttons button=json.fromJson(ECSEventInput.Buttons.class, string[0]);
        ECSEventInput.States state=json.fromJson(ECSEventInput.States.class, string[1]);

        if (button == ECSEventInput.Buttons.LEFT) {
            if (state == ECSEventInput.States.DOWN) {
                updateSword();
            }
            else if (state == ECSEventInput.States.UP) {
                setActiveSword(false);
            }
        }
        else if (button == ECSEventInput.Buttons.RIGHT) {
            if (state == ECSEventInput.States.DOWN || state == ECSEventInput.States.PRESSED) {
                player.elementLightActivate(Float.parseFloat(string[2]), Float.parseFloat(string[3]));
            }
            else if (state == ECSEventInput.States.UP) {
                player.elementLightSetActive(false);
            }
        }
    }

 */
}
