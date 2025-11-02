package com.noodlegamer76.engine.gltf.geometry;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.noodlegamer76.engine.gltf.material.McMaterial;
import com.noodlegamer76.engine.mixin.accessor.LightTextureAccessor;
import com.noodlegamer76.engine.mixin.accessor.VertexBufferAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;

import java.util.List;

public class GltfVbo extends VertexBuffer {
    private final McMaterial material;
    private int vertexCount;
    private int indicesCount;
    private List<Integer> usedJoints;

    public GltfVbo(Usage usage, McMaterial material) {
        super(usage);
        this.material = material;
    }

    public void instanceDraw(int instanceCount) {
        RenderSystem.assertOnRenderThread();
        ShaderInstance shader = getMaterial().getShaderRef().shader;

        if (shader.PROJECTION_MATRIX != null) shader.PROJECTION_MATRIX.set(RenderSystem.getProjectionMatrix());
        if (shader.COLOR_MODULATOR != null) shader.COLOR_MODULATOR.set(RenderSystem.getShaderColor());
        if (shader.GLINT_ALPHA != null) shader.GLINT_ALPHA.set(RenderSystem.getShaderGlintAlpha());
        if (shader.SCREEN_SIZE != null) {
            Window window = Minecraft.getInstance().getWindow();
            shader.SCREEN_SIZE.set((float) window.getWidth(), (float) window.getHeight());
        }

        RenderSystem.setupShaderLights(shader);
        shader.apply();

        bind();

        int indexCount = ((VertexBufferAccessor) this).getIndexCount();
        if (indexCount > 0) {
            GL31.glDrawElementsInstanced(GL15.GL_TRIANGLES, indexCount, GL15.GL_UNSIGNED_INT, 0L, instanceCount);
        } else {
            GL31.glDrawArraysInstanced(GL15.GL_TRIANGLES, 0, vertexCount, instanceCount);
        }

        unbind();
        shader.clear();
    }

    public void draw(Matrix4f modelViewMatrix, Matrix4f projectionMatrix, int packedLight) {
        ResourceLocation lightTexture = ((LightTextureAccessor) Minecraft.getInstance().gameRenderer.lightTexture()).getLightmapId();
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        int lightTextureId = textureManager.getTexture(lightTexture).getId();
        material.getShaderRef().shader.setSampler("lightTex", lightTextureId);

        RenderSystem.assertOnRenderThread();
        ShaderInstance shader = getMaterial().getShaderRef().shader;

        if (shader.MODEL_VIEW_MATRIX != null) {
            shader.MODEL_VIEW_MATRIX.set(modelViewMatrix);
        }

        if (shader.PROJECTION_MATRIX != null) {
            shader.PROJECTION_MATRIX.set(projectionMatrix);
        }

        if (shader.COLOR_MODULATOR != null) {
            shader.COLOR_MODULATOR.set(RenderSystem.getShaderColor());
        }

        if (shader.GLINT_ALPHA != null) {
            shader.GLINT_ALPHA.set(RenderSystem.getShaderGlintAlpha());
        }

        if (shader.FOG_START != null) {
            shader.FOG_START.set(RenderSystem.getShaderFogStart());
        }

        if (shader.FOG_END != null) {
            shader.FOG_END.set(RenderSystem.getShaderFogEnd());
        }

        if (shader.FOG_COLOR != null) {
            shader.FOG_COLOR.set(RenderSystem.getShaderFogColor());
        }

        if (shader.FOG_SHAPE != null) {
            shader.FOG_SHAPE.set(RenderSystem.getShaderFogShape().getIndex());
        }

        if (shader.TEXTURE_MATRIX != null) {
            shader.TEXTURE_MATRIX.set(RenderSystem.getTextureMatrix());
        }

        if (shader.GAME_TIME != null) {
            shader.GAME_TIME.set(RenderSystem.getShaderGameTime());
        }

        if (shader.SCREEN_SIZE != null) {
            Window window = Minecraft.getInstance().getWindow();
            shader.SCREEN_SIZE.set((float)window.getWidth(), (float)window.getHeight());
        }

        setLightUv(packedLight);

        RenderSystem.setupShaderLights(shader);
        shader.apply();

        draw();
        shader.clear();
    }

    private void setLightUv(int packedLight) {
        Uniform lightUv = material.getShaderRef().shader.getUniform("lightUv");
        if (lightUv != null) {
            int blockLight = packedLight & 0xFFFF;
            int skyLight   = (packedLight >> 16) & 0xFFFF;
            lightUv.set(blockLight, skyLight);
        }

        Uniform packedLightUniform = material.getShaderRef().shader.getUniform("packedLight");
        if (packedLightUniform != null) {
            packedLightUniform.set(packedLight);
        }
    }

    public void setUsedJoints(List<Integer> usedJoints) {
        this.usedJoints = usedJoints;
    }

    public List<Integer> getUsedJoints() {
        return usedJoints;
    }

    public McMaterial getMaterial() {
        return material;
    }

    public void setIndicesCount(int indicesCount) {
        this.indicesCount = indicesCount;
    }

    public void setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
    }

    public int getIndicesCount() {
        return indicesCount;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
