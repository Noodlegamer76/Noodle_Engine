package com.noodlegamer76.engine.gltf.material;

import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;
import org.joml.Vector4f;

/**
 * Predefined property keys for materials.
 * Extendable for custom properties.
 */
public final class MaterialProperty<T> {
    private final String name;
    private final Class<T> type;

    public MaterialProperty(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() { return name; }
    public Class<T> getType() { return type; }

    // PBR properties
    public static final MaterialProperty<ResourceLocation> ALBEDO_MAP = new MaterialProperty<>("albedoMap", ResourceLocation.class);
    public static final MaterialProperty<ResourceLocation> NORMAL_MAP = new MaterialProperty<>("normalMap", ResourceLocation.class);
    public static final MaterialProperty<ResourceLocation> METALLIC_MAP = new MaterialProperty<>("metallicMap", ResourceLocation.class);
    public static final MaterialProperty<ResourceLocation> ROUGHNESS_MAP = new MaterialProperty<>("roughnessMap", ResourceLocation.class);
    public static final MaterialProperty<ResourceLocation> AO_MAP = new MaterialProperty<>("aoMap", ResourceLocation.class);
    public static final MaterialProperty<ResourceLocation> EMISSIVE_MAP = new MaterialProperty<>("emissiveMap", ResourceLocation.class);
    public static final MaterialProperty<Float> METALLIC = new MaterialProperty<>("metallic", Float.class);
    public static final MaterialProperty<Float> ROUGHNESS = new MaterialProperty<>("roughness", Float.class);
    public static final MaterialProperty<Float> AO = new MaterialProperty<>("ao", Float.class);
    public static final MaterialProperty<Vector4f> BASE_COLOR_FACTOR = new MaterialProperty<>("baseColorFactor", Vector4f.class);
    public static final MaterialProperty<Vector4f> EMISSIVE_FACTOR = new MaterialProperty<>("emissiveFactor", Vector4f.class);
    public static final MaterialProperty<Boolean> TRANSPARENT  = new MaterialProperty<>("transparent", Boolean.class);
}