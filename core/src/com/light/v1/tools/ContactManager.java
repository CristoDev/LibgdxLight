package com.light.v1.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.TimeUtils;
import com.light.v1.ecs.ECSEvent;
import com.light.v1.ecs.LightObjectEntity;
import com.light.v1.ecs.LightPlayerEntity;
import com.light.v1.ecs.SystemManager;
import com.light.v1.element.Sign;

public class ContactManager implements ContactListener {
    //private static final String TAG = "ContactManager";

    @Override
    public void beginContact(Contact contact) {
        // EN COURS à voir comment mieux gérer les types --> ameliorer le tests des contacts (player/water...)
        /*
        Object data=contact.getFixtureA().getBody().getUserData();
        String message=" // " + contact.getFixtureA().getBody().getUserData().toString() + " --> " + contact.getFixtureB().getBody().getUserData().toString();


        if (data.getClass() == Sign.class) {
            message="Panneau trouvé! "+((Sign) data).getMessage() + message;
        }
        */

        manageBeginContact(contact.getFixtureA().getBody().getUserData(), contact.getFixtureB().getBody().getUserData());
    }

    @Override
    public void endContact(Contact contact) {
        String message=" // " + contact.getFixtureA().getBody().getUserData().toString() + " --> " + contact.getFixtureB().getBody().getUserData().toString();
        //Gdx.app.debug("endContact",  (TimeUtils.millis()%100000) + message);
        manageEndContact(contact.getFixtureA().getBody().getUserData(), contact.getFixtureB().getBody().getUserData());
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // presolve
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // postsolve
    }

    private void manageBeginContact(Object userDataA, Object userDataB) {
        if (userDataA.getClass() == LightObjectEntity.class) {
            Object property=((LightObjectEntity)userDataA).getProperty("speed");
            // ajouter le type comme propriété : grass/water --> traitement en fonction du type

            if (property != null) {
                SystemManager.getInstance().sendMessage(((LightPlayerEntity) userDataB), ECSEvent.Event.SPEED_MODIFIER, property.toString());
            }
        }
    }

    // temporaire à améliorer
    private void manageEndContact(Object userDataA, Object userDataB) {
        if (userDataA.getClass() == LightObjectEntity.class) {
            Object property = ((LightObjectEntity) userDataA).getProperty("speed");

            if (property != null) {
                SystemManager.getInstance().sendMessage(((LightPlayerEntity) userDataB), ECSEvent.Event.SPEED_MODIFIER_REVERSE, property.toString());
            }
        }
    }
}
