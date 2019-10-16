package components;

import com.badlogic.gdx.Gdx;
import com.light.v1.LightPlayer;

public abstract class ComponentInput extends ComponentSender implements ComponentReceiver {
    private static final String TAG = "<ABSTRACT>ComponentInput";

    public abstract void update(LightPlayer lightPlayer, float delta);

    @Override
    public void receiveMessage(String event, String message) {
        Gdx.app.debug(TAG, "Event: "+event+" // Message: "+message);
    }

}
