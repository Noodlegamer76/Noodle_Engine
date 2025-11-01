package com.noodlegamer76.engine.client.renderer.gltf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.engine.gltf.geometry.GltfVbo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

public class LightUvSsbo {
    private final int ssboId;
    private final IntBuffer buffer;
    private static final int MAX_LIGHTS = 8192;

    public LightUvSsbo() {
        ssboId = GL15.glGenBuffers();
        buffer = BufferUtils.createIntBuffer(MAX_LIGHTS * 2);

        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, ssboId);
        GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, (long) MAX_LIGHTS * 2 * Integer.BYTES, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);

        GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, 1, ssboId);
    }

    public void upload() {
        List<RenderableBuffer> buffers = GlbRenderer.getMatrixSsbo().getBuffers();
        Level level = Minecraft.getInstance().level;

        List<Vector2i> uvs = new ArrayList<>();

        for (RenderableBuffer buffer: buffers) {
            if (level == null) {
                uvs.add(new Vector2i(15, 15));
                continue;
            }

            int block;
            int sky;
            if (buffer.isUseLevelLight()) {
                Vector3f translation = getTranslation(buffer.getModelMatrix());
                block = level.getBrightness(LightLayer.BLOCK, BlockPos.containing(translation.x, translation.y, translation.z));
                sky = level.getBrightness(LightLayer.SKY, BlockPos.containing(translation.x, translation.y, translation.z));
            }
            else {
                int packedLight = buffer.getPackedLight();
                block = LightTexture.block(packedLight);
                sky = LightTexture.sky(packedLight);
            }

            Vector2i uv = new Vector2i(block, sky);
            uvs.add(uv);
        }

        uploadUvsToGpu(uvs);
    }

    public static Vector3f getTranslation(Matrix4f matrix) {
        return new Vector3f(matrix.m30(), matrix.m31(), matrix.m32());
    }

    private void uploadUvsToGpu(List<Vector2i> uvs) {
        buffer.clear();

        int count = Math.min(uvs.size(), MAX_LIGHTS);
        for (int i = 0; i < count; i++) {
            Vector2i uv = uvs.get(i);
            buffer.put(uv.x).put(uv.y);
        }

        for (int i = count; i < MAX_LIGHTS; i++) {
            buffer.put(15).put(15);
        }

        buffer.flip();

        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, ssboId);
        GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, 0, buffer);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
    }

    public int getSsboId() {
        return ssboId;
    }

    public IntBuffer getBuffer() {
        return buffer;
    }

    public static int getMaxLights() {
        return MAX_LIGHTS;
    }
}
