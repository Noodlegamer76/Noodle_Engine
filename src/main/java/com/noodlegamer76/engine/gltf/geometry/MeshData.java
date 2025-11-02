package com.noodlegamer76.engine.gltf.geometry;

import com.mojang.blaze3d.vertex.VertexBuffer;
import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.material.McMaterial;
import com.noodlegamer76.engine.gltf.node.Node;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeshData {
    private final Map<McMaterial, List<MeshPrimitiveModel>> primitives = new HashMap<>();
    private final Map<McMaterial, GltfVbo> primitiveBuffers = new HashMap<>();
    private final MeshModel meshModel;
    private final McGltf gltf;
    private Node node;

    public MeshData(MeshModel meshModel, McGltf gltf) {
        this.meshModel = meshModel;
        this.gltf = gltf;
    }

    public Map<McMaterial, GltfVbo> getPrimitiveBuffers() {
        return primitiveBuffers;
    }

    public Map<McMaterial, List<MeshPrimitiveModel>> getPrimitives() {
        return primitives;
    }

    public void addPrimitive(McMaterial material, MeshPrimitiveModel primitive) {
        if (!primitives.containsKey(material)) {
            primitives.put(material, new ArrayList<>());
        }
        primitives.get(material).add(primitive);
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void addPrimitiveBuffer(McMaterial material, GltfVbo buffer) {
        primitiveBuffers.put(material, buffer);
    }

    public void close() {
        primitiveBuffers.values().forEach(VertexBuffer::close);
    }

    public MeshModel getMeshModel() {
        return meshModel;
    }

    public McGltf getGltf() {
        return gltf;
    }
}
