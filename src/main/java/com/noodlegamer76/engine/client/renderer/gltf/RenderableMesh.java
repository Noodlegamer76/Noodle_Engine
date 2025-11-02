package com.noodlegamer76.engine.client.renderer.gltf;

import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.animation.skins.LoadSkins;
import com.noodlegamer76.engine.gltf.geometry.GltfVbo;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.*;

public class RenderableMesh {
    private final List<RenderableBuffer> buffers = new ArrayList<>();
    private final MeshData meshData;
    private final McGltf gltf;
    private List<Matrix4f> jointMatrices;
    private List<Integer> usedJoints = new ArrayList<>();
    private Matrix4f modelMatrix;

    public RenderableMesh(MeshData meshData) {
        this.meshData = meshData;
        this.gltf = meshData.getGltf();
    }

    public List<RenderableBuffer> getBuffers() {
        return buffers;
    }

    public MeshData getMeshData() {
        return meshData;
    }

    public void addBuffer(RenderableBuffer buffer) {
        buffers.add(buffer);
    }

    public void buildUsedJoints() {
        Set<Integer> jointSet = new HashSet<>();

        for (RenderableBuffer buffer : buffers) {
            GltfVbo vbo = buffer.getVertexBuffer();
            if (vbo != null && vbo.getUsedJoints() != null) {
                jointSet.addAll(vbo.getUsedJoints());
            }
        }

        List<Integer> usedJoints = new ArrayList<>(jointSet);
        Collections.sort(usedJoints);

        this.usedJoints = usedJoints;

        LoadSkins.loadSkins(this);
    }

    public McGltf getGltf() {
        return gltf;
    }

    public void setJointMatrices(List<Matrix4f> jointMatrices) {
        this.jointMatrices = jointMatrices;
    }

    @Nullable
    public List<Matrix4f> getJointMatrices() {
        return jointMatrices;
    }

    public List<Integer> getUsedJoints() {
        return usedJoints;
    }

    public void setModelMatrix(Matrix4f modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }
}
