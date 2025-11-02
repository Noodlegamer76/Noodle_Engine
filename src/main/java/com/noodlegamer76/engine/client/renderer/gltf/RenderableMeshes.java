package com.noodlegamer76.engine.client.renderer.gltf;

import java.util.ArrayList;
import java.util.List;

public class RenderableMeshes {
    private static final List<RenderableMesh> MESHES = new ArrayList<>();

    public static List<RenderableMesh> getMeshes() {
        return MESHES;
    }

    public static void addMesh(RenderableMesh mesh) {
        MESHES.add(mesh);
    }

    public static void removeAll() {
        MESHES.clear();
        GlbRenderer.clear();
    }

    public static void remove(RenderableMesh mesh) {
        MESHES.remove(mesh);
    }
}
