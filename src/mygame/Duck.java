/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.util.Random;

/**
 *
 * @author Sylvius
 */
public class Duck {

    MotionPath path;
    MotionEvent motionControl;
    public boolean end_of_path = false;

    public Duck() {
    }

    public Geometry CreateDuck(AssetManager assetManager,float x, float y) {
        Box boxMesh = new Box(1f, 1f, 0.1f);
        final Geometry duck = new Geometry("A Badly Textured Duck", boxMesh);
        Material boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        boxMat.setTexture("ColorMap", assetManager.loadTexture("Textures/duck.png"));
        duck.setMaterial(boxMat);
        duck.setQueueBucket(RenderQueue.Bucket.Transparent);
        duck.setLocalTranslation(x, y, -10);
        end_of_path = false;

        path = new MotionPath();
        Random r = new Random();

        for (int i = 0; i < 10; i++) {
            path.addWayPoint(new Vector3f(r.nextInt(10) - 5, r.nextInt(10) - 2, r.nextInt(10) - 9));
        }

        motionControl = new MotionEvent(duck, path);
        motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
        motionControl.setSpeed(0.5f);
        motionControl.setInitialDuration(5f);
        motionControl.play();

        path.addListener(new MotionPathListener() {
            public void onWayPointReach(MotionEvent control, int wayPointIndexa) {
                if (path.getNbWayPoints() == wayPointIndexa + 1) {
                    end_of_path = true;
                } else {
                    //Reached waypoint x
                    //do stuff      
                }
            }
        });
        return duck;
    }
}
