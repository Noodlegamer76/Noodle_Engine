package com.noodlegamer76.engine.gltf.animation.skins;

import com.noodlegamer76.engine.client.renderer.gltf.GlbRenderer;
import com.noodlegamer76.engine.client.renderer.gltf.RenderableBuffer;
import com.noodlegamer76.engine.client.renderer.gltf.RenderableMesh;
import com.noodlegamer76.engine.client.renderer.gltf.RenderableMeshes;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL43;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkinSsbo {
    private final int skinMatricesId;
    private final FloatBuffer skinMatrices;
    private final int startIndicesId;
    private final IntBuffer startIndices;
    private final int MAX_MATRICES = 8192;

    public SkinSsbo() {
        skinMatrices = BufferUtils.createFloatBuffer(MAX_MATRICES * 16);
        startIndices = BufferUtils.createIntBuffer(MAX_MATRICES);

        skinMatricesId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, skinMatricesId);
        GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, (long) MAX_MATRICES * 16 * Float.BYTES, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);

        startIndicesId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, startIndicesId);
        GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, (long) MAX_MATRICES * Integer.BYTES, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
    }

    public void build() {
        skinMatrices.clear();
        startIndices.clear();

        List<RenderableMesh> meshes = RenderableMeshes.getMeshes();

        Map<RenderableMesh, Integer> meshStarts = new HashMap<>();

        Matrix4f identity = new Matrix4f().transpose();
        float[] test = new float[16];
        identity.get(test);
        skinMatrices.put(test);
        int matrixOffset = 1;

        for (RenderableMesh mesh : meshes) {
            List<Integer> usedJoints = mesh.getJoints();
            if (usedJoints == null || usedJoints.isEmpty()) continue;

            meshStarts.put(mesh, matrixOffset);

            List<Matrix4f> jointMatrices = mesh.getJointMatrices();
            for (Integer usedJoint : usedJoints) {
                if (matrixOffset >= MAX_MATRICES) {
                    System.err.println("Warning: MAX_MATRICES exceeded, clipping skinning data");
                    break;
                }

                int jointIndex = usedJoint;
                Matrix4f m = (jointMatrices != null && jointIndex < jointMatrices.size()) ?
                        jointMatrices.get(jointIndex) : identity;
                if (m == null) m = identity;

                float[] arr = new float[16];
                m.get(arr);
                skinMatrices.put(arr);
                matrixOffset++;
            }
        }

        List<RenderableBuffer> buffers = GlbRenderer.getMatrixSsbo().getBuffers();
        for (RenderableBuffer buffer : buffers) {
            RenderableMesh mesh = buffer.getMesh();
            List<Integer> usedJoints = mesh.getJoints();

            if (usedJoints == null || usedJoints.isEmpty()) {
                startIndices.put(0);
            } else {
                Integer start = meshStarts.get(mesh);
                startIndices.put(start != null ? start : 0);
            }
        }

        upload();
    }


    private void upload() {
        int matricesCount = Math.min(skinMatrices.position() / 16, MAX_MATRICES);
        skinMatrices.limit(matricesCount * 16);
        skinMatrices.flip();

        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, skinMatricesId);
        GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, 0, skinMatrices);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);

        int indicesCount = Math.min(startIndices.position(), MAX_MATRICES);
        startIndices.limit(indicesCount);
        startIndices.flip();

        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, startIndicesId);
        GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, 0, startIndices);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
    }

    public void destroy() {
        GL15.glDeleteBuffers(skinMatricesId);
        GL15.glDeleteBuffers(startIndicesId);
    }


    public FloatBuffer getSkinMatrices() {
        return skinMatrices;
    }

    public int getMaxMatrices() {
        return MAX_MATRICES;
    }

    public int getSkinMatricesId() {
        return skinMatricesId;
    }

    public int getStartIndicesId() {
        return startIndicesId;
    }

    public IntBuffer getStartIndices() {
        return startIndices;
    }
}
