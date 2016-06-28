/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Sylvius
 */
public class PewPew extends Node {

    public static final Quaternion PITCH045 = new Quaternion().fromAngleAxis(FastMath.PI / 4, new Vector3f(1, 0, 0));

    public PewPew() {
    }

    public Geometry initGun(AssetManager assetManager) {
        /* A colored lit cube. Needs light source! */
        Box gunMesh = new Box(0.25f, 2f, 0.5f);
        Geometry gunGeo = new Geometry("Colored Box", gunMesh);
        Material gunMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        gunMat.setBoolean("UseMaterialColors", true);
        gunMat.setColor("Ambient", ColorRGBA.Green);
        gunMat.setColor("Diffuse", ColorRGBA.Green);
        gunGeo.setMaterial(gunMat);
        gunGeo.setName("M1911");
        gunGeo.setLocalTranslation(0, 0, 2f);
        gunGeo.setLocalRotation(PITCH045);
        return gunGeo;
    }

    public void RotateGun(Spatial gun, float x, float y) {
        Quaternion temp = new Quaternion();
        int tempx = 0, tempy = 0;
        if (x > 800) {
            tempx = -45;
        } else if (x <= 800) {
            tempx = 45;
        }
        temp.fromAngles(-45, tempx, 0);
        gun.setLocalRotation(temp);
    }
}
