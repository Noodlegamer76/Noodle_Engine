package com.noodlegamer76.engine.gltf.geometry;

import com.mojang.blaze3d.vertex.*;
import com.mojang.blaze3d.vertex.MeshData;
import com.noodlegamer76.engine.client.ModVertexFormat;
import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.material.MaterialProperty;
import com.noodlegamer76.engine.gltf.material.McMaterial;
import com.noodlegamer76.engine.mixin.BufferBuilderMixin;
import de.javagl.jgltf.model.*;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;
import org.lwjgl.system.MemoryUtil;

import java.nio.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VBORenderer {

    public static GltfVbo render(McGltf gltf, McMaterial material, List<MeshPrimitiveModel> primitives) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.TRIANGLES, ModVertexFormat.GLB_PBR);

        GltfVbo vbo = new GltfVbo(VertexBuffer.Usage.STATIC, material);

        renderPrimitive(gltf, bufferBuilder, primitives, material, vbo);

        MeshData meshData = bufferBuilder.build();
        if (meshData == null) return null;

        vbo.bind();
        vbo.upload(meshData);
        VertexBuffer.unbind();

        return vbo;
    }

    private static void renderPrimitive(McGltf gltf, BufferBuilder bb, List<MeshPrimitiveModel> primitives, McMaterial material, GltfVbo vbo) {
        for (MeshPrimitiveModel primitive : primitives) {
            int[] indices = GltfAccessorUtils.getIndexArray(primitive.getIndices());

            AccessorModel posAccessor = primitive.getAttributes().get("POSITION");
            AccessorModel normAccessor = primitive.getAttributes().get("NORMAL");
            AccessorModel weightsAccessor = primitive.getAttributes().get("WEIGHTS_0");
            AccessorModel jointsAccessor = primitive.getAttributes().get("JOINTS_0");

            float[] positions = GltfAccessorUtils.getFloatArray(posAccessor);
            float[] normals = GltfAccessorUtils.getFloatArray(normAccessor);
            float[] weights = GltfAccessorUtils.getFloatArray(weightsAccessor);
            int[] joints = GltfAccessorUtils.getJointIndexArray(jointsAccessor);

            vbo.setIndicesCount(indices == null ? 0 : indices.length);
            vbo.setVertexCount(positions == null ? 0 : positions.length / 3);

            Map<Integer, float[]> uvLayers = new HashMap<>();
            primitive.getAttributes().forEach((name, accessor) -> {
                if (name.toUpperCase().startsWith("TEXCOORD")) {
                    String idxStr = name.replaceAll("[^0-9]", "");
                    int layer = idxStr.isEmpty() ? 0 : Integer.parseInt(idxStr);
                    float[] uv = GltfAccessorUtils.getFloatArray(accessor);
                    if (uv != null) uvLayers.put(layer, uv);
                }
            });

            int vertexCount = positions.length / 3;

            if (indices != null) {
                for (int idx : indices) {
                    if (idx < 0 || idx >= vertexCount) continue;
                    renderVertex(idx, positions, normals, uvLayers, joints, weights, bb, material);
                }
            } else {
                for (int i = 0; i < vertexCount; i++) {
                    renderVertex(i, positions, normals, uvLayers, joints, weights, bb, material);
                }
            }
        }
    }

    private static void renderVertex(int i, float[] pos, float[] norm, Map<Integer, float[]> uvs, int[] joints, float[] weights, BufferBuilder bb, McMaterial mat) {
        float x = pos[i * 3], y = pos[i * 3 + 1], z = pos[i * 3 + 2];
        float nx = (norm != null) ? norm[i * 3] : 0f;
        float ny = (norm != null) ? norm[i * 3 + 1] : 0f;
        float nz = (norm != null) ? norm[i * 3 + 2] : 1f;

        Map<Integer, Vector2f> vertexUVs = new HashMap<>();
        uvs.forEach((layer, arr) -> {
            if (i * 2 + 1 < arr.length) vertexUVs.put(layer, new Vector2f(arr[i * 2], arr[i * 2 + 1]));
        });

        float[] j = new float[4];
        float[] w = new float[4];

        if (joints != null) for (int k = 0; k < 4; k++) j[k] = (i * 4 + k < joints.length) ? joints[i * 4 + k] : 0f;
        if (weights != null) for (int k = 0; k < 4; k++) w[k] = (i * 4 + k < weights.length) ? weights[i * 4 + k] : 0f;

        float sum = w[0] + w[1] + w[2] + w[3];
        if (sum > 0f) for (int k = 0; k < 4; k++) w[k] /= sum;

        render(bb, x, y, z, nx, ny, nz, vertexUVs, j, w, mat);
    }

    private static void render(BufferBuilder bb, float x, float y, float z, float nx, float ny, float nz, Map<Integer, Vector2f> vertexUVs, float[] joints, float[] weights, McMaterial mat) {
        Vector2f albedo = getUv(vertexUVs, mat, MaterialProperty.ALBEDO_MAP);
        Vector2f normal = getUv(vertexUVs, mat, MaterialProperty.NORMAL_MAP);
        Vector2f metallic = getUv(vertexUVs, mat, MaterialProperty.METALLIC_MAP);
        Vector2f roughness = getUv(vertexUVs, mat, MaterialProperty.ROUGHNESS_MAP);
        Vector2f ao = getUv(vertexUVs, mat, MaterialProperty.AO_MAP);
        Vector2f emissive = getUv(vertexUVs, mat, MaterialProperty.EMISSIVE_MAP);

        bb.addVertex(x, y, z);
        bb.setColor(255, 255, 255, 255);
        bb.setUv(albedo.x, albedo.y);
        bb.setNormal(nx, ny, nz);

        setUv(bb, ModVertexFormat.ELEMENT_NORMAL_UV, normal);
        setUv(bb, ModVertexFormat.ELEMENT_METALLIC_UV, metallic);
        setUv(bb, ModVertexFormat.ELEMENT_ROUGHNESS_UV, roughness);
        setUv(bb, ModVertexFormat.ELEMENT_AO_UV, ao);
        setUv(bb, ModVertexFormat.ELEMENT_EMISSIVE_UV, emissive);

        setVec4(bb, ModVertexFormat.JOINTS, joints);
        setVec4(bb, ModVertexFormat.WEIGHTS, weights);
    }

    private static Vector2f getUv(Map<Integer, Vector2f> uvs, McMaterial mat, MaterialProperty<?> prop) {
        if (!mat.hasTexCoord((MaterialProperty<ResourceLocation>) prop)) return new Vector2f(0.5f, 0.5f);
        int idx = mat.getTexCoord((MaterialProperty<ResourceLocation>) prop);
        return uvs.getOrDefault(idx, uvs.getOrDefault(0, new Vector2f(0.5f, 0.5f)));
    }


    private static void setUv(BufferBuilder bb, VertexFormatElement elem, Vector2f uv) {
        long ptr = ((BufferBuilderMixin) bb).invokeBeginElement(elem);
        if (ptr != -1L) {
            MemoryUtil.memPutFloat(ptr, uv.x);
            MemoryUtil.memPutFloat(ptr + 4L, uv.y);
        }
    }

    private static void setVec4(BufferBuilder bb, VertexFormatElement elem, float[] arr) {
        long ptr = ((BufferBuilderMixin) bb).invokeBeginElement(elem);
        if (ptr != -1L) {
            for (int i = 0; i < 4; i++) MemoryUtil.memPutFloat(ptr + i * 4L, arr[i]);
        }
    }
}
