package com.noodlegamer76.engine.gltf.animation.animation;

import com.noodlegamer76.engine.gltf.animation.animation.AnimationTrack;
import com.noodlegamer76.engine.gltf.node.Node;
import de.javagl.jgltf.model.AnimationModel;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class AnimationSampler {
    private static final Quaternionf scratchRotation = new Quaternionf();
    private static final Vector3f scratchVector = new Vector3f();

    public static Matrix4f sample(AnimationClip clip, Node node, float time) {
        List<AnimationTrack> tracks = clip.getNodeTrackMap().getOrDefault(node, new ArrayList<>());

        Vector3f translation = new Vector3f();
        Quaternionf rotation = new Quaternionf();
        Vector3f scale = new Vector3f(1.0f);

        for (AnimationTrack track : tracks) {
            if (track.getPath().equals("translation")) {
                translation.set(sampleVector(track, time));
            }
            if (track.getPath().equals("rotation")) {
                rotation.set(sampleRotation(track, time));
            }
            if (track.getPath().equals("scale")) {
                scale.set(sampleVector(track, time));
            }
        }

        return new Matrix4f().translationRotateScale(translation, rotation, scale);
    }

    public static Vector3f sampleVector(AnimationTrack track, float time) {
        float clampedTime = clampTime(track, time);

        if (track.getInterpolationType() == AnimationModel.Interpolation.LINEAR) {
            return sampleLinear(track, clampedTime, scratchVector);
        }

        if (track.getInterpolationType() == AnimationModel.Interpolation.STEP) {
            return sampleStep(track, clampedTime, scratchVector);
        }

        // TODO: Implement CUBICSPLINE

        return sampleLinear(track, clampedTime, scratchVector);
    }

    public static Quaternionf sampleRotation(AnimationTrack track, float time) {
        float clampedTime = clampTime(track, time);

        if (track.getInterpolationType() == AnimationModel.Interpolation.LINEAR) {
            return sampleSlerp(track, clampedTime, scratchRotation);
        }

        if (track.getInterpolationType() == AnimationModel.Interpolation.STEP) {
            return sampleStep(track, clampedTime, scratchRotation);
        }

        // TODO: Implement CUBICSPLINE (Requires fetching tangents and more complex math)

        return sampleSlerp(track, clampedTime, scratchRotation);
    }

    private static float clampTime(AnimationTrack track, float time) {
        float[] times = track.getKeyframeTimes();
        if (times.length == 0) return 0.0f;

        float maxTime = times[times.length - 1];
        return Math.min(time, maxTime);
    }

    /**
     * Finds the index of the keyframe whose time stamp is just before or equal to the current time.
     * @param track The track being sampled.
     * @param time The current animation time.
     * @return The index of the first keyframe for interpolation (k).
     */
    private static int findKeyframeIndex(AnimationTrack track, float time) {
        float[] times = track.getKeyframeTimes();

        for (int i = 0; i < times.length - 1; i++) {
            if (time < times[i + 1]) {
                return i;
            }
        }
        return times.length - 2;
    }

    /**
     * Samples the value using STEP interpolation (uses the value of the keyframe k).
     */
    @SuppressWarnings("unchecked")
    private static <T> T sampleStep(AnimationTrack track, float time, T out) {
        int k = findKeyframeIndex(track, time);
        List<T> values = (List<T>) track.getKeyframeValues();

        if (values.get(k) instanceof Vector3f) {
            ((Vector3f) out).set((Vector3f) values.get(k));
        } else if (values.get(k) instanceof Quaternionf) {
            ((Quaternionf) out).set((Quaternionf) values.get(k));
        }
        return out;
    }

    private static Vector3f sampleLinear(AnimationTrack track, float time, Vector3f out) {
        int k = findKeyframeIndex(track, time);
        float[] times = track.getKeyframeTimes();
        @SuppressWarnings("unchecked")
        List<Vector3f> values = (List<Vector3f>) track.getKeyframeValues();

        Vector3f vK = values.get(k);
        Vector3f vK1 = values.get(k + 1);

        float tK = times[k];
        float tK1 = times[k + 1];

        if (tK == tK1) {
            out.set(vK);
            return out;
        }

        float alpha = (time - tK) / (tK1 - tK);

        vK.lerp(vK1, alpha, out);
        return out;
    }

    private static Quaternionf sampleSlerp(AnimationTrack track, float time, Quaternionf out) {
        int k = findKeyframeIndex(track, time);
        float[] times = track.getKeyframeTimes();
        @SuppressWarnings("unchecked")
        List<Quaternionf> values = (List<Quaternionf>) track.getKeyframeValues();

        Quaternionf qK = values.get(k);
        Quaternionf qK1 = values.get(k + 1);

        float tK = times[k];
        float tK1 = times[k + 1];

        if (tK == tK1) {
            out.set(qK);
            return out;
        }

        float alpha = (time - tK) / (tK1 - tK);

        qK.slerp(qK1, alpha, out);
        return out;
    }
}
