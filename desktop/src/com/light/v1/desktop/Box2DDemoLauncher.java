package com.light.v1.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import demo.Box2DDemoMain;


public class Box2DDemoLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title="LightGame POC v0.1";
        config.useGL30=false;
        config.width=1027;
        config.height=768;

        Gdx.app=new LwjglApplication(new Box2DDemoMain(), config);;
        //Gdx.app.setLogLevel(Application.LOG_INFO);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        //Gdx.app.setLogLevel(Application.LOG_ERROR);
        //Gdx.app.setLogLevel(Application.LOG_NONE);

    }
}
