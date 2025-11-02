package com.noodlegamer76.engine.client.renderer.gltf;

import com.mojang.blaze3d.vertex.VertexBuffer;
import com.noodlegamer76.engine.gltf.geometry.GltfVbo;
import org.joml.Matrix4f;

public class RenderableBuffer {
    private Matrix4f modelMatrix;
    private int packedLight;
    private final GltfVbo vertexBuffer;
    private boolean useLevelLight;
    private final RenderableMesh mesh;

    public RenderableBuffer(Matrix4f modelMatrix, GltfVbo vertexBuffer, RenderableMesh mesh, int packedLight, boolean useLevelLight) {
        this.modelMatrix = modelMatrix;
        this.vertexBuffer = vertexBuffer;
        this.packedLight = packedLight;
        this.useLevelLight = useLevelLight;
        this.mesh = mesh;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public GltfVbo getVertexBuffer() {
        return vertexBuffer;
    }

    public int getPackedLight() {
        return packedLight;
    }

    public void setModelMatrix(Matrix4f modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public void setPackedLight(int packedLight) {
        this.packedLight = packedLight;
    }

    public boolean isUseLevelLight() {
        return useLevelLight;
    }

    public void setUseLevelLight(boolean useLevelLight) {
        this.useLevelLight = useLevelLight;
    }

    public RenderableMesh getMesh() {
        return mesh;
    }
}
