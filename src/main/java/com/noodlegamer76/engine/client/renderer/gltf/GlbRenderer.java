package com.noodlegamer76.engine.client.renderer.gltf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.engine.gltf.animation.skins.SkinSsbo;
import com.noodlegamer76.engine.gltf.geometry.GltfVbo;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.material.McMaterial;
import com.noodlegamer76.engine.gltf.node.Node;
import org.joml.Matrix4f;

import java.util.Map;

public class GlbRenderer {
    private static final MaterialBatch BATCH = new MaterialBatch();
    private static final MatrixSsbo MATRIX_SSBO = new MatrixSsbo();
    private static final LightUvSsbo LIGHT_UV_SSBO = new LightUvSsbo();
    private static final SkinSsbo SKIN_SSBO = new SkinSsbo();

    public static MaterialBatch getBatch() {
        return BATCH;
    }

    public static void clear() {
        BATCH.clear();
    }

    public static void addBuffer(McMaterial material, RenderableBuffer buffer) {
        BATCH.add(material, buffer);
    }

    /*
    make packedLight '-1' to match world light
     */
    public static void addInstance(MeshData meshData, PoseStack poseStack, int packedLight) {
        Node node = meshData.getNode();
        RenderableMesh renderableMesh = new RenderableMesh(meshData);
        Matrix4f modelMatrix = new Matrix4f(poseStack.last().pose()).mul(node.getModelMatrix());
        for (Map.Entry<McMaterial, GltfVbo> buffers: meshData.getPrimitiveBuffers().entrySet()) {
            RenderableBuffer renderableBuffer = new RenderableBuffer(modelMatrix, buffers.getValue(), renderableMesh, packedLight, packedLight == -1);
            addBuffer(buffers.getKey(), renderableBuffer);
            renderableMesh.addBuffer(renderableBuffer);
        }
        renderableMesh.buildUsedJoints();
        renderableMesh.setModelMatrix(modelMatrix);
        RenderableMeshes.addMesh(renderableMesh);
    }

    public static MatrixSsbo getMatrixSsbo() {
        return MATRIX_SSBO;
    }

    public static LightUvSsbo getLightUvSsbo() {
        return LIGHT_UV_SSBO;
    }

    public static SkinSsbo getSkinSsbo() {
        return SKIN_SSBO;
    }
}
