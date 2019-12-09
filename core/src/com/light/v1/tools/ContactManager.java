package com.light.v1.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.TimeUtils;
import com.light.v1.element.Sign;

public class ContactManager implements ContactListener {
    //private static final String TAG = "ContactManager";

    @Override
    public void beginContact(Contact contact) {
        // @todo à voir comment mieux gérer les types --> ameliorer le tests des contacts (player/water...)
        Object data=contact.getFixtureA().getBody().getUserData();
        String message=" // " + contact.getFixtureA().getBody().getUserData().toString() + " --> " + contact.getFixtureB().getBody().getUserData().toString();

        if (data.getClass() == Sign.class) {
            message="Panneau trouvé! "+((Sign) data).getMessage() + message;
        }

        Gdx.app.debug("beginContact",  (TimeUtils.millis()%100000) + message);
    }

    @Override
    public void endContact(Contact contact) {
        String message=" // " + contact.getFixtureA().getBody().getUserData().toString() + " --> " + contact.getFixtureB().getBody().getUserData().toString();
        Gdx.app.debug("endContact",  (TimeUtils.millis()%100000) + message);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // presolve
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // postsolve
    }

    /*
    private void manageContact(Object userDataA, Object userDataB) {
        // manage contact
    }
     */
}
