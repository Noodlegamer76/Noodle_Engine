package com.noodlegamer76.engine.gltf.material;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.mixin.accessor.LightTextureAccessor;
import de.javagl.jgltf.model.MaterialModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.joml.*;
import org.lwjgl.opengl.GL20;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class McMaterial {
    private final ShaderReference shaderRef;
    private final String name;
    private final Map<String, Object> properties = new HashMap<>();
    private MaterialModel materialModel;

    public McMaterial(ShaderReference shader, String name, Map<String, Object> properties) {
        this.shaderRef = shader;
        this.name = name;
        if (properties != null) this.properties.putAll(properties);
    }

    public void bind() {
        RenderSystem.setShader(() -> shaderRef.shader);

        ResourceLocation lightTexture = ((LightTextureAccessor) Minecraft.getInstance().gameRenderer.lightTexture()).getLightmapId();
        int lightTextureId = Minecraft.getInstance().getTextureManager().getTexture(lightTexture).getId();
        shaderRef.shader.setSampler("lightTex", lightTextureId);


        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();

        setUniform(MaterialProperty.ROUGHNESS);
        setUniform(MaterialProperty.METALLIC);
        setUniform(MaterialProperty.AO);
        setUniform(MaterialProperty.EMISSIVE_FACTOR);
        setUniform(MaterialProperty.BASE_COLOR_FACTOR);

        for (MaterialProperty<ResourceLocation> prop : List.of(
                MaterialProperty.ALBEDO_MAP,
                MaterialProperty.NORMAL_MAP,
                MaterialProperty.METALLIC_MAP,
                MaterialProperty.ROUGHNESS_MAP,
                MaterialProperty.AO_MAP,
                MaterialProperty.EMISSIVE_MAP)) {

            if (hasProperty(prop)) {
                setSampler(prop);
            }
            else {
                shaderRef.shader.setSampler(prop.getName(), ResourceLocation.withDefaultNamespace("textures/block/dirt.png"));
            }

            Uniform hasAlbedoUniform = shaderRef.shader.getUniform("hasAlbedo");
            if (prop.getName().equals("albedoMap") && hasAlbedoUniform != null) {
                if (hasProperty(prop)) {
                    hasAlbedoUniform.set(1);
                }
                else {
                    hasAlbedoUniform.set(0);
                }
            }
        }

        shaderRef.shader.setSampler("depthTex", Minecraft.getInstance().getMainRenderTarget().getDepthTextureId());

        Matrix4f projectionInverse = new Matrix4f(RenderSystem.getProjectionMatrix()).invert();
        Uniform projInverseUniform = shaderRef.shader.getUniform("projMatInverse");
        if (projInverseUniform != null) {
            projInverseUniform.set(projectionInverse);
        }

        Matrix4f modelViewInverse = new Matrix4f(RenderSystem.getModelViewMatrix()).invert();
        Uniform modelViewInverseUniform = shaderRef.shader.getUniform("modelViewMatInverse");
        if (modelViewInverseUniform != null) {
            modelViewInverseUniform.set(modelViewInverse);
        }

        Uniform cameraPos = shaderRef.shader.getUniform("cameraPos");
        if (cameraPos != null) {
            cameraPos.set(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().toVector3f());
        }
    }

    public void setUniform(MaterialProperty<?> property) {
        Uniform uniform = shaderRef.shader.getUniform(property.getName());
        if (uniform != null && hasProperty(property) && getProperty(property) != null) {
            Class<?> type = property.getType();

            if (type == Float.class) {
                uniform.set((float) getProperty(property));
            }

            else if (type == Integer.class) {
                uniform.set((int) getProperty(property));
            }

            else if (type == Vector2f.class) {
                Vector2f vec2f = (Vector2f) getProperty(property);
                uniform.set(vec2f.x, vec2f.y);
            }

            else if (type == Vector3f.class) {
                Vector3f vec3f = (Vector3f) getProperty(property);
                uniform.set(vec3f.x, vec3f.y, vec3f.z);
            }

            else if (type == Vector4f.class) {
                Vector4f vec4f = (Vector4f) getProperty(property);
                uniform.set(vec4f.x, vec4f.y, vec4f.z, vec4f.w);
            }

            else if (type == Matrix3f.class) {
                Matrix3f matrix3f = (Matrix3f) getProperty(property);
                uniform.set(matrix3f);
            }

            else if (type == Matrix4f.class) {
                Matrix4f matrix4f = (Matrix4f) getProperty(property);
                uniform.set(matrix4f);
            }

            else {
                NoodleEngine.LOGGER.warn("Could not set uniform " + property.getName() + " of type " + type.getSimpleName());
            }
        }
    }

    public void setSampler(MaterialProperty<ResourceLocation> property) {
        if (!hasProperty(property)) return;

        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        int id = textureManager.getTexture(getProperty(property)).getId();
        shaderRef.shader.setSampler(property.getName(), id);
    }

    public void setTexCoord(MaterialProperty<ResourceLocation> property, int texCoordIndex) {
        properties.put(property.getName() + "_texCoord", texCoordIndex);
    }

    public boolean hasTexCoord(MaterialProperty<ResourceLocation> property) {
        return properties.containsKey(property.getName() + "_texCoord");
    }

    public int getTexCoord(MaterialProperty<ResourceLocation> property) {
        return (int) properties.get(property.getName() + "_texCoord");
    }

    public ShaderReference getShaderRef() {
        return shaderRef;
    }

    public void setShader(ShaderInstance shaderRef) {
        this.shaderRef.shader = shaderRef;
    }

    public <T> void setProperty(MaterialProperty<T> property, T value) {
        properties.put(property.getName(), value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(MaterialProperty<T> property) {
        return (T) properties.get(property.getName());
    }

    public boolean hasProperty(MaterialProperty<?> property) {
        return properties.containsKey(property.getName());
    }

    public String getName() {
        return name;
    }

    public void setMaterialModel(MaterialModel materialModel) {
        this.materialModel = materialModel;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public MaterialModel getMaterialModel() {
        return materialModel;
    }
}