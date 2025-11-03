package com.noodlegamer76.engine.gltf.animation.animation;

import com.noodlegamer76.engine.gltf.node.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationClip {

    private final String name;
    private final float durationSeconds;
    private final List<AnimationTrack> tracks = new ArrayList<>();
    private final Map<Node, List<AnimationTrack>> nodeTrackMap = new HashMap<>();

    public AnimationClip(String name, float durationSeconds) {
        this.name = name;
        this.durationSeconds = durationSeconds;
    }

    public void addTrack(AnimationTrack track) {
        this.tracks.add(track);

        Node node = track.getNode();
        this.nodeTrackMap
                .computeIfAbsent(node, k -> new ArrayList<>())
                .add(track);
    }

    public String getName() {
        return name;
    }

    public float getDurationSeconds() {
        return durationSeconds;
    }

    public List<AnimationTrack> getTracks() {
        return tracks;
    }

    public Map<Node, List<AnimationTrack>> getNodeTrackMap() {
        return nodeTrackMap;
    }
}
