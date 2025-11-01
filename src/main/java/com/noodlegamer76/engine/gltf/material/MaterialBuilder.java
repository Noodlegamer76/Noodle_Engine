package com.noodlegamer76.engine.gltf.material;

import de.javagl.jgltf.model.MaterialModel;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class MaterialBuilder {
    private final ShaderReference shader;
    private final String name;
    private final Map<String, Object> properties = new HashMap<>();

    public MaterialBuilder(ShaderReference shader, String name) {
        this.shader = shader;
        this.name = name;
    }

    public MaterialBuilder setTexCoord(MaterialProperty<ResourceLocation> property, int texCoordIndex) {
        properties.put(property.getName() + "_texCoord", texCoordIndex);
        return this;
    }

    public <T> MaterialBuilder set(MaterialProperty<T> property, T value) {
        properties.put(property.getName(), value);
        return this;
    }

    public MaterialBuilder texture(ResourceLocation texturePath) {
        return set(MaterialProperty.ALBEDO_MAP, texturePath);
    }

    public MaterialBuilder normalMap(ResourceLocation normalPath) {
        return set(MaterialProperty.NORMAL_MAP, normalPath);
    }

    public MaterialBuilder metallicMap(ResourceLocation metallicPath) {
        return set(MaterialProperty.METALLIC_MAP, metallicPath);
    }

    public MaterialBuilder roughnessMap(ResourceLocation roughnessPath) {
        return set(MaterialProperty.ROUGHNESS_MAP, roughnessPath);
    }

    public MaterialBuilder aoMap(ResourceLocation aoPath) {
        return set(MaterialProperty.AO_MAP, aoPath);
    }

    public MaterialBuilder emissiveMap(ResourceLocation emissivePath) {
        return set(MaterialProperty.EMISSIVE_MAP, emissivePath);
    }

    public MaterialBuilder metallic(float metallic) {
        return set(MaterialProperty.METALLIC, metallic);
    }

    public MaterialBuilder roughness(float roughness) {
        return set(MaterialProperty.ROUGHNESS, roughness);
    }

    public MaterialBuilder transparent(boolean transparent) {
        return set(MaterialProperty.TRANSPARENT, transparent);
    }

    public McMaterial build(MaterialModel materialModel) {
        McMaterial material = new McMaterial(shader, name, properties);
        material.setMaterialModel(materialModel);
        return material;
    }
}