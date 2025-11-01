package com.noodlegamer76.engine.client.renderer.gltf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.engine.gltf.geometry.GltfVbo;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43; // for SSBO

import java.nio.FloatBuffer;
import java.util.*;

public class MatrixSsbo {

    private final int ssboId;
    private final FloatBuffer buffer;
    private final Map<GltfVbo, Integer> bufferOffsets = new HashMap<>();
    private final List<RenderableBuffer> buffers = new ArrayList<>();
    private static final int MAX_MATRICES = 8192;
    private static final Matrix4f IDENTITY = new Matrix4f().identity();
    private final Map<RenderableBuffer, Matrix4f> bufferToMatrix = new HashMap<>();

    public MatrixSsbo() {
        ssboId = GL15.glGenBuffers();
        buffer = BufferUtils.createFloatBuffer(MAX_MATRICES * 16);

        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, ssboId);
        GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, (long) MAX_MATRICES * 16 * Float.BYTES, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);

        GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, 0, ssboId);
    }

    public void upload(PoseStack poseStack) {
        bufferOffsets.clear();
        bufferToMatrix.clear();
        buffers.clear();

        Map<GltfVbo, Integer> offsets = new HashMap<>();
        List<Matrix4f> matrices = new ArrayList<>();

        Matrix4f view = poseStack.last().pose();

        MaterialBatch batch = GlbRenderer.getBatch();

        batch.getBuffers().forEach((material, buffers) -> {
            for (Map.Entry<GltfVbo, List<RenderableBuffer>> entry : buffers.entrySet()) {
                GltfVbo vbo = entry.getKey();
                List<RenderableBuffer> renderableBuffers = entry.getValue();

                offsets.put(vbo, matrices.size());

                for (RenderableBuffer renderableBuffer : renderableBuffers) {
                    if (matrices.size() >= MAX_MATRICES) break;
                    Matrix4f modelView = new Matrix4f(view).mul(renderableBuffer.getModelMatrix());
                    matrices.add(modelView);
                    bufferToMatrix.put(renderableBuffer, modelView);
                    this.buffers.add(renderableBuffer);
                }
            }
        });

        bufferOffsets.putAll(offsets);
        uploadMatricesToGpu(matrices);
    }

    private void uploadMatricesToGpu(List<Matrix4f> matrices) {
        buffer.clear();
        float[] tmp = new float[16];

        for (int i = 0; i < MAX_MATRICES; i++) {
            if (i < matrices.size()) {
                matrices.get(i).get(tmp);
            } else {
                IDENTITY.get(tmp);
            }
            buffer.put(tmp);
        }

        buffer.flip();

        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, ssboId);
        GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, 0, buffer);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
    }

    public int getSsboId() {
        return ssboId;
    }

    public Map<GltfVbo, Integer> getBufferOffsets() {
        return Collections.unmodifiableMap(bufferOffsets);
    }

    public static int getMaxMatrices() {
        return MAX_MATRICES;
    }

    public void destroy() {
        GL15.glDeleteBuffers(ssboId);
    }

    public FloatBuffer getBuffer() {
        return buffer;
    }

    public Map<RenderableBuffer, Matrix4f> getBufferToMatrix() {
        return bufferToMatrix;
    }

    public List<RenderableBuffer> getBuffers() {
        return buffers;
    }
}
