package com.noodlegamer76.engine.gltf.load;

import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.animation.animation.AnimationClip;
import com.noodlegamer76.engine.gltf.animation.animation.AnimationTrack;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.AnimationModel;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to load glTF animation data into custom Animation and AnimationTrack objects.
 */
public class LoadAnimations {

    public static void loadAnimations(McGltf gltf) {
        List<AnimationModel> gltfAnimations = gltf.getModel().getAnimationModels();

        for (AnimationModel gltfAnimation : gltfAnimations) {

            float maxTime = 0.0f;
            for (AnimationModel.Channel channel : gltfAnimation.getChannels()) {
                AccessorModel inputAccessor = channel.getSampler().getInput();
                Number[] max = inputAccessor.getMax();

                if (max != null && max.length > 0) {
                    maxTime = Math.max(maxTime, max[0].floatValue());
                }
            }

            AnimationClip animation = new AnimationClip(gltfAnimation.getName(), maxTime);
            for (AnimationModel.Channel channel : gltfAnimation.getChannels()) {
                AnimationModel.Sampler sampler = channel.getSampler();

                AccessorModel inputAccessor = sampler.getInput();
                float[] keyframeTimes = getFloatArray(gltf, inputAccessor);

                AccessorModel outputAccessor = sampler.getOutput();
                String path = channel.getPath();

                List<?> keyframeValues;

                if (path.equals("rotation")) {
                    keyframeValues = loadRotationKeyframes(gltf, outputAccessor);
                } else if (path.equals("translation") || path.equals("scale")) {
                    keyframeValues = loadVectorKeyframes(gltf, outputAccessor);
                } else {
                    System.err.println("Unsupported animation path: " + path);
                    continue;
                }

                // Node is retrieved via gltf.getNodeModelToNode() lookup
                AnimationTrack track = new AnimationTrack(
                        gltf.getNodeModelToNode().get(channel.getNodeModel()),
                        path,
                        sampler.getInterpolation(),
                        keyframeTimes,
                        keyframeValues
                );

                animation.addTrack(track);
            }

            gltf.getAnimations().put(animation.getName(), animation);
        }
    }

    private static float[] getFloatArray(McGltf gltf, AccessorModel accessor) {
        // --- FIX 2: Use duplicate() to safely read a shared buffer ---
        FloatBuffer originalBuffer = (FloatBuffer) gltf.getAccessorBuffers().get(accessor);
        FloatBuffer floatBuffer = originalBuffer.duplicate();

        float[] array = new float[floatBuffer.remaining()];
        floatBuffer.get(array);
        return array;
    }

    private static List<Quaternionf> loadRotationKeyframes(McGltf gltf, AccessorModel accessor) {
        FloatBuffer originalBuffer = (FloatBuffer) gltf.getAccessorBuffers().get(accessor);
        FloatBuffer floatBuffer = originalBuffer.duplicate();

        List<Quaternionf> rotations = new ArrayList<>(accessor.getCount());

        for (int i = 0; i < accessor.getCount(); i++) {
            float x = floatBuffer.get();
            float y = floatBuffer.get();
            float z = floatBuffer.get();
            float w = floatBuffer.get();

            rotations.add(new Quaternionf(x, y, z, w));
        }
        return rotations;
    }

    private static List<Vector3f> loadVectorKeyframes(McGltf gltf, AccessorModel accessor) {
        FloatBuffer originalBuffer = (FloatBuffer) gltf.getAccessorBuffers().get(accessor);
        FloatBuffer floatBuffer = originalBuffer.duplicate();

        List<Vector3f> vectors = new ArrayList<>(accessor.getCount());

        for (int i = 0; i < accessor.getCount(); i++) {
            float x = floatBuffer.get();
            float y = floatBuffer.get();
            float z = floatBuffer.get();

            vectors.add(new Vector3f(x, y, z));
        }
        return vectors;
    }
}
