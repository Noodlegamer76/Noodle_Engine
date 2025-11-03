package com.noodlegamer76.engine.gltf.animation.animation;

import com.noodlegamer76.engine.client.renderer.gltf.RenderableMesh;
import com.noodlegamer76.engine.gltf.animation.skins.LoadSkins;
import com.noodlegamer76.engine.gltf.node.Node;
import de.javagl.jgltf.model.NodeModel;
import org.joml.Matrix4f;

import java.util.*;

public class SingleAnimationPlayer {
    private final AnimationClip clip;
    private float currentTimeSeconds;
    private final RenderableMesh mesh;

    private final Map<Node, Matrix4f> animatedSkinMatrices = new HashMap<>();

    public SingleAnimationPlayer(RenderableMesh mesh, AnimationClip clip) {
        this.mesh = mesh;
        this.clip = clip;
        this.currentTimeSeconds = 0.0f;
    }

    public void update(float timeDeltaSeconds) {
        currentTimeSeconds += timeDeltaSeconds;

        if (currentTimeSeconds > clip.getDurationSeconds()) {
            currentTimeSeconds = 0;
        }

        calculateAnimatedGlobalTransforms();
    }

    public void calculateAnimatedGlobalTransforms() {
        List<NodeModel> jointNodeModels = mesh.getMeshData().getSkin().getJoints();

        Set<Node> processedNodes = new HashSet<>();

        for (NodeModel jointModel : jointNodeModels) {

            Node rootNode = mesh.getGltf().getNodeModelToNode().get(jointModel);
            while (rootNode.getParent() != null) {
                rootNode = rootNode.getParent();
            }

            if (processedNodes.add(rootNode)) {
                calculateRecursive(rootNode, new Matrix4f().identity());
            }
        }
    }
    private void calculateRecursive(Node node, Matrix4f parentAnimatedGlobal) {
        Matrix4f localAnimated = AnimationSampler.sample(clip, node, currentTimeSeconds);

        Matrix4f animatedGlobal = parentAnimatedGlobal.mul(localAnimated, new Matrix4f());

        animatedSkinMatrices.put(node, animatedGlobal);

        for (NodeModel childModel : node.getNodeModel().getChildren()) {
            Node child = mesh.getGltf().getNodeModelToNode().get(childModel);
            calculateRecursive(child, animatedGlobal);
        }
    }

    public Matrix4f getAnimatedSkinMatrix(Node node) {
        return animatedSkinMatrices.getOrDefault(node, new Matrix4f().identity());
    }

    public Matrix4f sample(Node node) {
        return getAnimatedSkinMatrix(node);
    }
}
