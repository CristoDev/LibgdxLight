package com.light.v1.ECS;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.light.v1.LightGame;
import com.light.v1.element.WorldTorch;
import com.light.v1.tools.MyMap;

public class LightPlayerPhysics extends LightPhysics {
    private static final String TAG = "LightPlayerPhysics";

    private Body bodyItem = null, bodySword=null, bodyCollision=null;
    private Light lightItem=null;
    private float candleAlpha=0.8f;
    private double itemDiag;
    private float maxFPS=60f;
    private Vector2 translate = new Vector2(0, 0);
    private double angle=Math.PI/2;
    private float swordWidth=10;

    /*
    utilisation de la classe
    - calcul de la direction, position et angle en fonction des touches et de ces informations en T-1
    - envoyer un message en indiquant l'angle et la direction
    - envoyer un message en fonction des actions (si besoin --> coup d'épée dans la bonne direction?)
     */

    private LightPlayerEntity player;

    public LightPlayerPhysics(LightPlayerEntity entity, World world, RayHandler rayHandler) {
        player=entity;
        init(world, rayHandler);
    }

    public void init(World world, RayHandler rayHandler) {
        itemDiag=Math.sqrt(Math.pow(player.getItemWidth()/2d, 2)*2);
        addItem(world, rayHandler);
        createSword(world);

    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "dispose");
    }

    @Override
    public void receiveMessage(ECSEvent.EVENT event, String message) {
        if (event == ECSEvent.EVENT.KEY_DIRECTION) {
            keyDirectionsPressed(message);
        }
        else if (event == ECSEvent.EVENT.KEY_ACTION) {
            keyActionsPressed(message);
        }
        else if (event == ECSEvent.EVENT.MOUSE_ACTION) {
            mouseButtonsPressed(message);
        }
    }

    @Override
    public void update(Batch batch) {
        int fps = Gdx.graphics.getFramesPerSecond();

        if (fps > 0 && fps < maxFPS) {
            float ratio=maxFPS / Gdx.graphics.getFramesPerSecond();
            bodyItem.setLinearVelocity(translate.x*ratio, translate.y*ratio);
        }
        else {
            bodyItem.setLinearVelocity(translate);
        }

        bodyItem.setTransform(bodyItem.getPosition().x, bodyItem.getPosition().y, (float)angle);
        lightItem.setPosition(bodyItem.getPosition().x, bodyItem.getPosition().y);

        // petit effet de bougie sur le cone de lumière
        candleAlpha= MathUtils.clamp(candleAlpha+MathUtils.random(-0.05f, 0.05f), 0.7f, 1f);
        lightItem.setColor(127f, 127f, 127f, candleAlpha);

        player.setPosition(bodyItem.getPosition());
    }

    @Override
    public void render(Batch batch) {
        // render
    }

    private void addItem(World world, RayHandler rayHandler) {
        PolygonShape boxItem = new PolygonShape();
        boxItem.setAsBox(player.getItemWidth()*0.8f * MyMap.UNIT_SCALE / 2, player.getItemWidth()*0.8f * MyMap.UNIT_SCALE / 2);
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.position.set(LightGame.VIEWPORT.viewportWidth / 2f, LightGame.VIEWPORT.viewportHeight / 2);
        boxBodyDef.type= BodyDef.BodyType.DynamicBody;

        bodyItem = world.createBody(boxBodyDef);
        FixtureDef boxFixtureDef = new FixtureDef();
        boxFixtureDef.shape = boxItem;
        boxFixtureDef.restitution = 0f;
        boxFixtureDef.density = 0f;
        bodyItem.createFixture(boxFixtureDef);
        bodyItem.setUserData(player);
        boxItem.dispose();

        lightItem = new ConeLight(rayHandler, 12, Color.GRAY, 5, 15, 5, 270, 30);
        lightItem.setSoft(false);
        lightItem.attachToBody(bodyItem);
    }

    // @todo modifier le code pour uniquement faire une action en fonction de l'équipement ou de l'objet devant (pnj/panneau/monstre)
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

        BodyDef collisionBodyDef=new BodyDef();
        collisionBodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyCollision=world.createBody(collisionBodyDef);
        bodyCollision.setActive(false);
        PolygonShape collisionShape=new PolygonShape();
        collisionShape.setAsBox(0.4f, 0.2f);

        FixtureDef collisionFixtureDef=new FixtureDef();
        collisionFixtureDef.shape=collisionShape;
        collisionFixtureDef.restitution=0f;
        collisionFixtureDef.density=0f;
        collisionFixtureDef.isSensor=true;
        bodyCollision.createFixture(collisionFixtureDef);
        bodyCollision.setUserData("Verification collision");

        collisionShape.dispose();
    }

    private double getAngle() {
        return angle;
    }

    private void setAngle(double angle) {
        this.angle = angle;
    }

    private void setTranslate(Vector2 translate) {
        this.translate = translate;
    }

    // @todo à déplacer dans une autre classe (action?)
    private void updateSword() {
        bodyCollision.setActive(false);
        bodyCollision.setTransform(bodyItem.getPosition().x+(float)Math.cos(bodyItem.getAngle()) * (1 + MyMap.UNIT_SCALE), bodyItem.getPosition().y+(float)Math.sin(bodyItem.getAngle())*(1+MyMap.UNIT_SCALE), bodyItem.getAngle());
        bodyCollision.setLinearDamping(5f);
        bodyCollision.setAngularVelocity(0.01f);
        bodyCollision.setActive(true);

        setActiveSword(false);
        bodySword.setTransform(bodyItem.getPosition().x+(float)Math.cos(bodyItem.getAngle())* MyMap.UNIT_SCALE, bodyItem.getPosition().y+(float)Math.sin(bodyItem.getAngle())*MyMap.UNIT_SCALE, bodyItem.getAngle());
        bodySword.setLinearDamping(5f);
        bodySword.setAngularVelocity(0.01f);
        bodySword.setActive(true);

        player.setPosition(bodyItem.getPosition());
    }

    private void setActiveSword(boolean active) {
        bodySword.setActive(active);
    }

    private void keyDirectionsPressed(String message) {
        String[] string = message.split(ECSEvent.MESSAGE_TOKEN);
        float velocity=player.getVelocity();
        double angle=getAngle();

        ECSEventInput.Keys direction = json.fromJson(ECSEventInput.Keys.class, string[0]);
        ECSEventInput.States state=json.fromJson(ECSEventInput.States.class, string[1]);

        if (direction == ECSEventInput.Keys.LEFT) {
            translate.x=0;
            if (state == ECSEventInput.States.DOWN || state == ECSEventInput.States.PRESSED) {
                translate.x = -velocity;
                angle = Math.PI - Math.PI/4*Math.signum(translate.y);
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

    private void keyActionsPressed(String message) {
        String[] string = message.split(ECSEvent.MESSAGE_TOKEN);
        ECSEventInput.Keys direction = json.fromJson(ECSEventInput.Keys.class, string[0]);
        ECSEventInput.States state=json.fromJson(ECSEventInput.States.class, string[1]);

        if (direction == ECSEventInput.Keys.SPACE &&  state == ECSEventInput.States.DOWN) {
            new WorldTorch(player.rayHandler, player.camera.position);
        }
    }

    private void mouseButtonsPressed(String message) {
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
}
