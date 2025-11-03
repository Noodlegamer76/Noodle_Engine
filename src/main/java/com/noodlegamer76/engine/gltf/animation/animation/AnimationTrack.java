package com.noodlegamer76.engine.gltf.animation.animation;

import com.noodlegamer76.engine.gltf.node.Node;
import de.javagl.jgltf.model.AnimationModel;

import java.util.List;

public class AnimationTrack {
    private final String path;
    private final Node node;
    private final AnimationModel.Interpolation interpolationType;
    private final float[] keyframeTimes;
    private final List<?> keyframeValues;

    public AnimationTrack(
            Node node,
            String path,
            AnimationModel.Interpolation interpolationType,
            float[] keyframeTimes,
            List<?> keyframeValues) {
        this.node = node;
        this.path = path;
        this.interpolationType = interpolationType;
        this.keyframeTimes = keyframeTimes;
        this.keyframeValues = keyframeValues;
    }

    public Node getNode() {
        return node;
    }

    public String getPath() {
        return path;
    }

    public AnimationModel.Interpolation getInterpolationType() {
        return interpolationType;
    }

    public float[] getKeyframeTimes() {
        return keyframeTimes;
    }
    public List<?> getKeyframeValues() {
        return keyframeValues;
    }
}
