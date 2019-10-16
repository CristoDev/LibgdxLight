package com.light.v1;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import tools.MyMap;

public class LightPlayerGraphics extends ObserverNotifier {
    private static final String TAG = "LightGraphics";
    private Sprite item = null;
    private float itemWidth=16, swordWidth=10;
    private Body bodyItem = null, bodySword=null;
    private Light lightItem=null;
    private float candleAlpha=0.8f;
    private double itemDiag;
    private float maxFPS=60f;
    private Vector2 translate = new Vector2(0, 0);
    private double angle=Math.PI/2;

    public LightPlayerGraphics() {
        itemDiag=Math.sqrt(Math.pow(itemWidth/2d, 2)*2);
    }

    public void update(LightPlayer lightPlayer, float delta) {
        //Gdx.app.debug(TAG, "void(0)");
        int fps = Gdx.graphics.getFramesPerSecond();

        if (fps > 0 && fps < maxFPS) {
            float ratio=maxFPS / Gdx.graphics.getFramesPerSecond();
            bodyItem.setLinearVelocity(translate.x*ratio, translate.y*ratio);
            //Gdx.app.debug(TAG, "FPS: "+fps+ "-- > "+ratio);
        }
        else {
            bodyItem.setLinearVelocity(translate);
        }

        bodyItem.setTransform(bodyItem.getPosition().x, bodyItem.getPosition().y, (float)angle);
        item.setPosition(bodyItem.getPosition().x - 16 * MyMap.UNIT_SCALE / 2, bodyItem.getPosition().y - 16 * MyMap.UNIT_SCALE / 2);
        lightItem.setPosition(bodyItem.getPosition().x, bodyItem.getPosition().y);
        // petit effet de bougie sur le cone de lumière
        candleAlpha= MathUtils.clamp(candleAlpha+MathUtils.random(-0.05f, 0.05f), 0.7f, 1f);
        lightItem.setColor(127f, 127f, 127f, candleAlpha);
    }

    public void receiveMessage(String event, String message) {
        Gdx.app.debug(TAG, "Message reçu: "+event+" // "+message);

    }


    public void addItem(World world, RayHandler rayHandler) {
        Texture texture = new Texture(Gdx.files.internal("item.png"));
        // @TODO creer une classe pour ajouter un nom au sprite et l'utiliser dans bodyItem.setUserData
        item = new Sprite(texture);
        item.setSize(item.getWidth() * MyMap.UNIT_SCALE, item.getHeight() * MyMap.UNIT_SCALE);

        PolygonShape boxItem = new PolygonShape();
        boxItem.setAsBox(itemWidth*0.8f * MyMap.UNIT_SCALE / 2, itemWidth*0.8f * MyMap.UNIT_SCALE / 2);
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.position.set(LightGame.VIEWPORT.viewportWidth / 2f, LightGame.VIEWPORT.viewportHeight / 2);
        boxBodyDef.type= BodyDef.BodyType.DynamicBody;

        bodyItem = world.createBody(boxBodyDef);
        FixtureDef boxFixtureDef = new FixtureDef();
        boxFixtureDef.shape = boxItem;
        boxFixtureDef.restitution = 0f;
        boxFixtureDef.density = 0f;
        bodyItem.createFixture(boxFixtureDef);
        bodyItem.setUserData(item);

        lightItem = new ConeLight(rayHandler, 12, Color.GRAY, 5, 15, 5, 270, 30);
        lightItem.setSoft(false);
        lightItem.attachToBody(bodyItem);

        item.setPosition(bodyItem.getPosition().x - itemWidth * MyMap.UNIT_SCALE / 2, bodyItem.getPosition().y - itemWidth * MyMap.UNIT_SCALE / 2);
        item.setBounds(item.getX(), item.getY(), item.getWidth(), item.getHeight());
    }

    public void createSword(World world) {
        float length=((float)itemDiag+swordWidth)*MyMap.UNIT_SCALE;

        Vector2[] swordAttack=new Vector2[]{
                new Vector2(0.5f, -0.5f), //A
                new Vector2(0.5f, 0.5f), // B
                new Vector2(length*(float)Math.cos(Math.PI/4), length*(float)Math.sin(Math.PI/4)), //G
                new Vector2(length*(float)Math.cos(Math.PI/8), length*(float)Math.sin(Math.PI/8)), //K
                new Vector2(length, 0), //F
                new Vector2(length*(float)Math.cos(-Math.PI/8), length*(float)Math.sin(-Math.PI/8)), //M
                new Vector2(length*(float)Math.cos(-Math.PI/4), length*(float)Math.sin(-Math.PI/4)), //H
        };

        BodyDef swordBodyDef = new BodyDef();
        swordBodyDef.type= BodyDef.BodyType.DynamicBody; // en attendant mieux -> utilisation des masques
        bodySword = world.createBody(swordBodyDef);
        bodySword.setActive(false);

        PolygonShape swordShape=new PolygonShape();
        swordShape.set(swordAttack);
//        bodySword.createFixture(swordShape, 0f);
        FixtureDef swordFixtureDef = new FixtureDef();
        swordFixtureDef.shape = swordShape;
        swordFixtureDef.restitution = 0f;
        swordFixtureDef.density = 0.0f;
        swordFixtureDef.isSensor=true;
        bodySword.createFixture(swordFixtureDef);
        bodySword.setUserData("coup de guiche-guiche");

        swordShape.dispose();
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setTranslate(Vector2 translate) {
        this.translate = translate;
    }

    public void updateSword() {
        bodySword.setTransform(bodyItem.getPosition().x+(float)Math.cos(bodyItem.getAngle())* MyMap.UNIT_SCALE, bodyItem.getPosition().y+(float)Math.sin(bodyItem.getAngle())*MyMap.UNIT_SCALE, bodyItem.getAngle());
        bodySword.setLinearDamping(5f);
        bodySword.setAngularVelocity(0.01f);
        bodySword.setActive(true);
    }

    public void setActiveSword(boolean active) {
        bodySword.setActive(active);
    }

    public Vector2 getPosition() {
        return bodyItem.getPosition();
    }

    public void render(Batch batch) {
        item.draw(batch);
    }
}
