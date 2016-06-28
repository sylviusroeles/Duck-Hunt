/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Sylvius
 */
public class Scenery extends Node {
    
    public Scenery(){
        
    }
    
    public Geometry CreateGrass(AssetManager assetManager){
        /** An unshaded textured cube. 
        *  Uses texture from jme3-test-data library! */ 
       Box boxMesh = new Box(8f,1.8f,0.1f); 
       Geometry grass = new Geometry("A Textured Grass Patch", boxMesh); 
       Material boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  
       boxMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
       boxMat.setTexture("ColorMap", assetManager.loadTexture("Textures/grass.png")); 
       grass.setMaterial(boxMat); 
       grass.setQueueBucket(Bucket.Transparent);
       return grass;
    }

    public Geometry CreatetTree(AssetManager assetManager){
        /** An unshaded textured cube. 
        *  Uses texture from jme3-test-data library! */ 
       Box boxMesh = new Box(3f,4f,0.1f); 
       Geometry tree = new Geometry("A Textured Tree", boxMesh); 
       Material boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  
       boxMat.setTexture("ColorMap", assetManager.loadTexture("Textures/tree.png"));
       boxMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
       tree.setMaterial(boxMat); 
       tree.setQueueBucket(Bucket.Transparent);
       tree.setLocalTranslation(-3.4f, 1.9f, -2f);
       return tree;
    }
}
