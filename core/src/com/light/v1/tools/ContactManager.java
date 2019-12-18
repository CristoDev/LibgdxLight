package com.light.v1.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.light.v1.ecs.*;
import com.light.v1.element.WorldManager;

public class ContactManager implements ContactListener {
    //private static final String TAG = "ContactManager";

    @Override
    public void beginContact(Contact contact) {
        manageBeginContact(contact.getFixtureA().getBody().getUserData(), contact.getFixtureB().getBody().getUserData());
    }

    @Override
    public void endContact(Contact contact) {
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
            speedProperty(userDataA, userDataB);
            //warpProperty(userDataA, userDataB);
        }
    }

    // temporaire à améliorer
    private void manageEndContact(Object userDataA, Object userDataB) {
        if (userDataA.getClass() == LightObjectEntity.class) {
            Object property = ((LightObjectEntity) userDataA).getProperty("speed");

            if (property != null) {
                SystemManager.getInstance().sendMessage(((LightPlayerEntity) userDataB), ECSEvent.Event.SPEED_MODIFIER_REVERSE, property.toString() + ECSEvent.MESSAGE_TOKEN + userDataA.toString());
            }
            warpProperty(userDataA, userDataB);
        }
    }

    private void speedProperty(Object userDataA, Object userDataB) {
        Object property=((LightObjectEntity)userDataA).getProperty("speed");
        // ajouter le type comme propriété : grass/water --> traitement en fonction du type
        if (property != null) {
            SystemManager.getInstance().sendMessage(((LightPlayerEntity) userDataB), ECSEvent.Event.SPEED_MODIFIER, property.toString() + ECSEvent.MESSAGE_TOKEN + userDataA.toString());
        }
    }

    private void warpProperty(Object userDataA, Object userDataB) {
        /*
        Object property=((LightObjectEntity)userDataA).getProperty("type");
        if (property != null) {
            if (property.toString().compareTo("warp_source") == 0) {
                LightEntity destination=WorldManager.getInstance().getWarp(((LightObjectEntity)userDataA).getProperty("to"));
                Vector2 position=destination.getPosition();
                Gdx.app.debug("WORLD", "changement de position vers " + position.x+"/"+position.y);
                SystemManager.getInstance().sendMessage(((LightPlayerEntity) userDataB), ECSEvent.Event.CHANGE_POSITION, position.x + ECSEvent.MESSAGE_TOKEN + position.y);
            }
        }

         */
    }
}
