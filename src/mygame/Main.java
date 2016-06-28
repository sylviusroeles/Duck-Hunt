package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.MotionPath;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * test
 *
 * @author sylviusroeles
 */
public class Main extends SimpleApplication {

    Scenery sc = new Scenery();
    PewPew pp = new PewPew();
    Duck duck = new Duck();
    Dog dog = new Dog();
    private Node mainScene;
    private Node dogNode;
    BitmapText score; //Crosshair
    BitmapText ch;
    BitmapText bullets;
    BitmapText ducksshot;
    BitmapText prepnotifier;
    MotionPath path;
    private Node shootables; //Node for ducks
    private int score_amount = 0;
    private int bullet_amount = 3;
    private int rounds_played = 0;
    private int rounds_won = 0;
    private int rounds_lost = 0;
    private int ducks_shot = 0;
    private int ducks_spawn = 0;
    private boolean round_over = true;
    private boolean can_shoot = false;
    private boolean show_prep_message = false;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        inputManager.setCursorVisible(true);
        initScenery();
        setCamera();
        initCrossHairs();
        initScore();
        initBulletAmount();
        initDuckShot();
        initKeys();
        RoundStartNotifier();
        rootNode.attachChild(pp.initGun(assetManager));
        rootNode.attachChild(sc.CreateGrass(assetManager));
        rootNode.attachChild(sc.CreatetTree(assetManager));
        shootables = new Node("Shootables");
        dogNode = new Node("Dog Node");
        rootNode.attachChild(shootables);
        rootNode.attachChild(dogNode);
        rootNode.attachChild(mainScene);
        duckspawnerthread.start();
    }
    Thread duckspawnerthread = new Thread() {
        @Override
        public void run() {
            try {
                DuckSpawner();
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    @Override
    public void simpleUpdate(float tpf) {
        Vector2f mousepos = new Vector2f(inputManager.getCursorPosition());
        if (mousepos.x > 10 && mousepos.y > 10) {
            if (mousepos.x < 1580 && mousepos.y < 920) {
                //ch.setText(mousepos.x+ " : "+ mousepos.y);
                ch.setLocalTranslation(new Vector3f(mousepos.x, mousepos.y, 0));
            } else if (mousepos.x > 1580) {
                org.lwjgl.input.Mouse.setCursorPosition(1580, Integer.parseInt(Math.round(mousepos.y) + ""));
            } else if (mousepos.y > 920) {
                org.lwjgl.input.Mouse.setCursorPosition(Integer.parseInt(Math.round(mousepos.x) + ""), 920);
            }
        } else if (mousepos.x < 0) {
            org.lwjgl.input.Mouse.setCursorPosition(0, Integer.parseInt(Math.round(mousepos.y) + ""));
        } else if (mousepos.y < 0) {
            org.lwjgl.input.Mouse.setCursorPosition(Integer.parseInt(Math.round(mousepos.x) + ""), 0);
        }
        pp.RotateGun(rootNode.getChild("M1911"), mousepos.x, mousepos.y);
        updateDuckShot();
        updateBullets(bullet_amount);
        updateScore(score_amount);
        if (!can_shoot) {
            guiNode.attachChild(prepnotifier);
            shootables.detachAllChildren();
        } else {
            guiNode.detachChild(prepnotifier);
            if(ducks_spawn < 2){
                SpawnDuck();
                ducks_spawn++;
            }
        }
        if (round_over && dogNode.getChildren().isEmpty()) {
            if (ducks_shot == 0) {
                dogNode.attachChild(dog.CreateDog(assetManager, "Textures/dog.png"));
            } else if (ducks_shot == 1) {
                dogNode.attachChild(dog.CreateDog(assetManager, "Textures/dog_w_1duck.png"));
            } else {
                dogNode.attachChild(dog.CreateDog(assetManager, "Textures/dog_w_2duck.png"));
            }
        }
        if (!round_over && !dogNode.getChildren().isEmpty()) {
            dogNode.detachAllChildren();
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private void initScenery() {
        mainScene = new Node();
        Texture west = assetManager.loadTexture("Textures/Sky/Left.png");
        Texture east = assetManager.loadTexture("Textures/Sky/Right.png");
        Texture north = assetManager.loadTexture("Textures/Sky/Front.png");
        Texture south = assetManager.loadTexture("Textures/Sky/Back.png");
        Texture up = assetManager.loadTexture("Textures/Sky/Top.png");
        Texture down = assetManager.loadTexture("Textures/Sky/Top.png");
        mainScene.attachChild(SkyFactory.createSky(assetManager, west, east, north, south, up, down));

        DirectionalLight sun = new DirectionalLight();
        Vector3f lightDir = new Vector3f(-0.37352666f, -0.50444174f, -0.7784704f);
        sun.setDirection(lightDir);
        sun.setColor(ColorRGBA.White.clone().multLocal(2));
        //mainScene.addLight(sun);
        rootNode.addLight(sun);
    }

    private void setCamera() {
        this.flyCam.setMoveSpeed(0);
        this.flyCam.setRotationSpeed(0);
        cam.setLocation(new Vector3f(-0.0054458375f, 1.7268517f, 7.967976f));
    }

    protected void initCrossHairs() {
        setDisplayStatView(false);
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        ch = new BitmapText(guiFont, false);
        ch.setLocalTranslation(0, 0, 0);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        guiNode.attachChild(ch);
    }

    protected void initScore() {
        setDisplayStatView(false);
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        score = new BitmapText(guiFont, false);
        score.setLocalTranslation(20, 850, 0);
        score.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        score.setText("Score: " + score_amount);
        guiNode.attachChild(score);
    }

    private void updateScore(int amount) {
        score.setText("Score: " + amount);
    }

    private void initBulletAmount() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        bullets = new BitmapText(guiFont, false);
        bullets.setLocalTranslation(20, 820, 0);
        bullets.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        bullets.setText("Bullets: " + bullet_amount);
        guiNode.attachChild(bullets);
    }

    private void updateBullets(int amount) {
        bullets.setText("Bullets: " + amount);

    }

    private void initDuckShot() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        ducksshot = new BitmapText(guiFont, false);
        ducksshot.setLocalTranslation(20, 790, 0);
        ducksshot.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ducksshot.setText("Games won/lost: " + rounds_won + "/" + rounds_lost);
        guiNode.attachChild(ducksshot);
    }

    private void updateDuckShot() {
        ducksshot.setText("Games won/lost: " + rounds_won + "/" + rounds_lost);
    }

    private void initKeys() {
        inputManager.addMapping("Shoot",
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
        inputManager.addListener(actionListener, "Shoot");
    }
    /**
     * Defining the "Shoot" action: Determine what was hit and how to respond.
     */
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Shoot") && !keyPressed && bullet_amount > 0 && can_shoot) {
                // 1. Reset results list.
                CollisionResults results = new CollisionResults();
                // 2. Aim the ray from cam loc to cam direction.
                Vector2f mouseCoords = new Vector2f(inputManager.getCursorPosition());
                Ray ray = new Ray(cam.getWorldCoordinates(mouseCoords, 0),
                        cam.getWorldCoordinates(mouseCoords, 1).subtractLocal(
                        cam.getWorldCoordinates(mouseCoords, 0)).normalizeLocal());
                // 3. Collect intersections between Ray and Shootables in results list.
                shootables.collideWith(ray, results);
                // 4. Print the results
                System.out.println("----- Collisions? " + results.size() + "-----");
                for (int i = 0; i < results.size(); i++) {
                    // For each hit, we know distance, impact point, name of geometry.
                    float dist = results.getCollision(i).getDistance();
                    Vector3f pt = results.getCollision(i).getContactPoint();
                    String hit = results.getCollision(i).getGeometry().getName();
                    System.out.println("* Collision #" + i);
                    System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
                }
                // 5. Use the results (we mark the hit object)
                if (results.size() > 0) {
                    //Update score
                    shootables.detachChild(results.getClosestCollision().getGeometry());
                    score_amount = score_amount + 100;
                    ducks_shot++;
                    bullet_amount--;
                    //remove bullet
                } else {
                    //Remove Bullet
                    bullet_amount--;
                }
            }
        }
    };

    private void RoundStartNotifier() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        prepnotifier = new BitmapText(guiFont, false);
        prepnotifier.setLocalTranslation(800, 790, 0);
        prepnotifier.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        prepnotifier.setText("Preparing Le Ducks");
    }

    private void DuckSpawner() throws InterruptedException {
        show_prep_message = true;
        Thread.sleep(5000);
        round_over = false;
        ducks_shot = 0;
        show_prep_message = false;
        can_shoot = true;
        duck.end_of_path = false;
        System.out.println(ducks_shot + " " + bullet_amount + " " + duck.end_of_path);
        while (!round_over) {
            if (ducks_shot == 2) {
                rounds_won++;
                //round_over = true;
                break;
            } else if (bullet_amount == 0 || duck.end_of_path) {
                rounds_lost++;
                //round_over = true;
                break;
            }
            System.out.println(ducks_shot + " " + bullet_amount + " " + duck.end_of_path);
        }
        System.out.println("end reached");
        round_over = true;
        can_shoot = false;
        ducks_spawn = 0;
        bullet_amount = 3;
        rounds_played++;
        DuckSpawner();
    }

    private void SpawnDuck() {
        Random r = new Random();
        shootables.attachChild(duck.CreateDuck(assetManager, r.nextInt(10) - 5, -3f));
    }
}
