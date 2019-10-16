package components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.light.v1.LightPlayer;

public class InputPlayer extends ComponentInput implements InputProcessor {
    /*
    à chaque update, on envoi un message pour dire quelle touche a ete utilisée et son état (up/down)


     */
    private static final String TAG = "InputPlayer";


    @Override
    public void dispose() {

    }

    @Override
    public void update(LightPlayer lightPlayer, float delta) {
        Gdx.app.debug(TAG, "udpate");
        // appel sendMessage ici
    }

    @Override
    public void receiveMessage(String event, String message) {
        Gdx.app.debug(TAG, "receiveMessage - Event: "+event+" // Message: "+message);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
