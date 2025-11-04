package com.noodlegamer76.engine.client.renderer.gltf;

import com.noodlegamer76.engine.gltf.geometry.GltfVbo;
import org.joml.Matrix4f;

public class RenderableBuffer {
    //skinned model matrix will later be multiplied with the animation globals
    private Matrix4f skinnedModelMatrix;
    private Matrix4f modelMatrix;
    private int packedLight;
    private final GltfVbo vertexBuffer;
    private boolean useLevelLight;
    private final RenderableMesh mesh;

    public RenderableBuffer(Matrix4f skinnedModelMatrix, Matrix4f modelMatrix, GltfVbo vertexBuffer, RenderableMesh mesh, int packedLight, boolean useLevelLight) {
        this.skinnedModelMatrix = skinnedModelMatrix;
        this.modelMatrix = modelMatrix;
        this.vertexBuffer = vertexBuffer;
        this.packedLight = packedLight;
        this.useLevelLight = useLevelLight;
        this.mesh = mesh;
    }

    public Matrix4f getSkinnedModelMatrix() {
        return skinnedModelMatrix;
    }

    public GltfVbo getVertexBuffer() {
        return vertexBuffer;
    }

    public int getPackedLight() {
        return packedLight;
    }

    public void setSkinnedModelMatrix(Matrix4f skinnedModelMatrix) {
        this.skinnedModelMatrix = skinnedModelMatrix;
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

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public void setModelMatrix(Matrix4f modelMatrix) {
        this.modelMatrix = modelMatrix;
    }
}
