package com.noodlegamer76.engine.gltf.animation.skins;

import com.noodlegamer76.engine.client.renderer.gltf.RenderableMesh;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.node.Node;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.impl.DefaultSkinModel;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.BufferUtils;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.*;

public class LoadSkins {
    public static void loadSkins(RenderableMesh mesh) {
        SkinModel skin = getSkinForMesh(mesh.getMeshData());
        if (skin == null) return;

        List<NodeModel> allGltfJointNodes = skin.getJoints();
        AccessorModel invBindMatricesAcc = skin.getInverseBindMatrices();

        List<Integer> usedJoints = mesh.getUsedJoints();

        if (usedJoints.isEmpty()) {
            mesh.setJointMatrices(Collections.emptyList());
            return;
        }

        FloatBuffer invBindMatrices = null;
        if (invBindMatricesAcc != null) {
            Object buf = mesh.getGltf().getAccessorBuffers().get(invBindMatricesAcc);

            if (buf instanceof ByteBuffer bb) {
                bb.order(ByteOrder.LITTLE_ENDIAN);
                invBindMatrices = bb.asFloatBuffer();
            } else if (buf instanceof FloatBuffer fb) {
                invBindMatrices = fb;
            }
        }

        int globalJointCount = allGltfJointNodes.size();
        if (invBindMatrices != null) {
            globalJointCount = Math.min(globalJointCount, invBindMatrices.capacity() / 16);
        }

        Map<NodeModel, Matrix4f> worldCache = new HashMap<>();

        Matrix4f[] finalJointMatrices = new Matrix4f[usedJoints.size()];

        for (int i = 0; i < usedJoints.size(); i++) {
            int globalJointIndex = usedJoints.get(i);

            if (globalJointIndex >= globalJointCount) {
                finalJointMatrices[i] = new Matrix4f().identity();
                continue;
            }

            NodeModel jointNode = allGltfJointNodes.get(globalJointIndex);
            Matrix4f jointWorld = computeWorldTransform(jointNode, worldCache);

            Matrix4f invBind = new Matrix4f().identity();
            if (invBindMatrices != null && invBindMatrices.capacity() >= (globalJointIndex + 1) * 16) {
                float[] arr = new float[16];
                invBindMatrices.position(globalJointIndex * 16);
                invBindMatrices.get(arr, 0, 16);

                invBind = new Matrix4f(
                        arr[0], arr[1], arr[2], arr[3],
                        arr[4], arr[5], arr[6], arr[7],
                        arr[8], arr[9], arr[10], arr[11],
                        arr[12], arr[13], arr[14], arr[15]
                );
            }

            finalJointMatrices[i] = new Matrix4f(jointWorld).mul(invBind);
        }

        mesh.setJointMatrices(Arrays.asList(finalJointMatrices));
    }

    private static Matrix4f computeWorldTransform(NodeModel node, Map<NodeModel, Matrix4f> cache) {
        if (cache.containsKey(node)) return new Matrix4f(cache.get(node));

        Matrix4f local = new Matrix4f();
        float[] mat = node.getMatrix();
        if (mat != null && mat.length >= 16) {
            local = new Matrix4f(BufferUtils.createFloatBuffer(16).put(mat).flip());
        } else {
            local.identity();
            float[] t = node.getTranslation();
            float[] r = node.getRotation();
            float[] s = node.getScale();

            if (t != null && t.length >= 3) local.translate(t[0], t[1], t[2]);
            if (r != null && r.length >= 4) local.rotate(new Quaternionf(r[0], r[1], r[2], r[3]));
            if (s != null && s.length >= 3) local.scale(s[0], s[1], s[2]);
        }

        NodeModel parent = node.getParent();
        Matrix4f world;
        if (parent != null) {
            world = new Matrix4f(computeWorldTransform(parent, cache)).mul(local);
        } else {
            world = new Matrix4f(local);
        }

        cache.put(node, new Matrix4f(world));
        return world;
    }

    @Nullable
    private static SkinModel getSkinForMesh(MeshData meshData) {
        Node node = meshData.getNode();
        if (node == null || node.getNodeModel() == null) return null;
        NodeModel nodeModel = node.getNodeModel();
        return nodeModel.getSkinModel();
    }
}