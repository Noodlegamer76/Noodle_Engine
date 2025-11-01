package com.noodlegamer76.engine.client.renderer.gltf;

import com.mojang.blaze3d.vertex.VertexBuffer;
import com.noodlegamer76.engine.gltf.geometry.GltfVbo;
import com.noodlegamer76.engine.gltf.material.McMaterial;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialBatch {
    private final Map<McMaterial, Map<GltfVbo, List<RenderableBuffer>>> buffers = new HashMap<>();

    public void add(McMaterial material, RenderableBuffer buffer) {
        if (!buffers.containsKey(material)) {
            buffers.put(material, new HashMap<>());
        }
        if (!buffers.get(material).containsKey(buffer.getVertexBuffer())) {
            buffers.get(material).put(buffer.getVertexBuffer(), new ArrayList<>());
        }
        buffers.get(material).get(buffer.getVertexBuffer()).add(buffer);
    }

    @Nullable
    public List<RenderableBuffer> getBuffers(McMaterial material, GltfVbo buffer) {
        return buffers.get(material).get(buffer);
    }

    public void remove(McMaterial material) {
        buffers.remove(material);
    }

    public Map<McMaterial, Map<GltfVbo, List<RenderableBuffer>>> getBuffers() {
        return buffers;
    }

    public boolean isEmpty() {
        return buffers.isEmpty();
    }

    public void clear() {
        buffers.clear();
    }
}
