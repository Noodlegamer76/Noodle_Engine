package com.noodlegamer76.engine.client.renderer.gltf;

import com.noodlegamer76.engine.gltf.material.McMaterial;

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
        for (RenderableBuffer buffer : mesh.getBuffers()) {
            McMaterial material = buffer.getVertexBuffer().getMaterial();
            GlbRenderer.getBatch().getBuffers().get(material).get(buffer.getVertexBuffer()).remove(buffer);
        }
    }
}
