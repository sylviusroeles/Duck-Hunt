/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
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
public class Dog {

    MotionPath path;
    MotionEvent motionControl;

    public Dog() {
    }

    public Geometry CreateDog(AssetManager assetManager, String texture) {
        /**
         * An unshaded textured cube. Uses texture from jme3-test-data library!
         */
        Box boxMesh = new Box(1f, 1f, 0.1f);
        Geometry dog = new Geometry("A Textured Grass Patch", boxMesh);
        Material boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        boxMat.setTexture("ColorMap", assetManager.loadTexture(texture));
        dog.setMaterial(boxMat);
        dog.setQueueBucket(RenderQueue.Bucket.Transparent);
        dog.setLocalTranslation(0, -3, -1);
        path = new MotionPath();
        path.addWayPoint(new Vector3f(0, -6, 0));
        path.addWayPoint(new Vector3f(0, 3, 0));
        path.addWayPoint(new Vector3f(0, -6, 0));
        
        motionControl = new MotionEvent(dog, path);
        motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
        motionControl.setSpeed(0.8f);
        motionControl.setInitialDuration(5f);
        motionControl.play();
        return dog;
    }
}
