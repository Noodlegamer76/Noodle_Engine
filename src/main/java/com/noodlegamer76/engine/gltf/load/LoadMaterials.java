package com.noodlegamer76.engine.gltf.load;

import com.mojang.blaze3d.platform.NativeImage;
import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.event.ShaderRegistry;
import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.material.MaterialBuilder;
import com.noodlegamer76.engine.gltf.material.MaterialProperty;
import com.noodlegamer76.engine.gltf.material.McMaterial;
import de.javagl.jgltf.impl.v2.*;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.TextureModel;
import de.javagl.jgltf.model.v2.MaterialModelV2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector4f;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.*;

public class LoadMaterials {
    public static void loadMaterials(McGltf gltf) {
        MaterialData materialData = gltf.getMaterialData();

        for (int i = 0; i < gltf.getModel().getMaterialModels().size(); i++) {
            MaterialModelV2 mat = (MaterialModelV2) gltf.getModel().getMaterialModels().get(i);

            String matName = mat.getName();
            if (matName == null) matName = "gltf_material_" + i;

            MaterialBuilder builder = new MaterialBuilder(ShaderRegistry.pbr, matName);

            TextureModel baseColor = mat.getBaseColorTexture();
            if (baseColor != null) {
                Integer texCoord = mat.getBaseColorTexcoord();
                ResourceLocation tex = gltf.getImageData().getTextureModelToTextures().get(baseColor);
                builder.set(MaterialProperty.ALBEDO_MAP, tex);
                builder.setTexCoord(MaterialProperty.ALBEDO_MAP, texCoord == null ? 0 : texCoord);
            }

            TextureModel normal = mat.getNormalTexture();
            if (normal != null) {
                Integer texCoord = mat.getNormalTexcoord();
                ResourceLocation tex = gltf.getImageData().getTextureModelToTextures().get(normal);
                builder.set(MaterialProperty.NORMAL_MAP, tex);
                builder.setTexCoord(MaterialProperty.NORMAL_MAP, texCoord == null ? 0 : texCoord);
            }

            TextureModel emissive = mat.getEmissiveTexture();
            if (emissive != null) {
                Integer texCoord = mat.getEmissiveTexcoord();
                ResourceLocation tex = gltf.getImageData().getTextureModelToTextures().get(emissive);
                builder.set(MaterialProperty.EMISSIVE_MAP, tex);
                builder.setTexCoord(MaterialProperty.EMISSIVE_MAP, texCoord == null ? 0 : texCoord);
            }

            TextureModel occlusion = mat.getOcclusionTexture();
            if (occlusion != null) {
                Integer texCoord = mat.getOcclusionTexcoord();
                ResourceLocation tex = gltf.getImageData().getTextureModelToTextures().get(occlusion);
                builder.set(MaterialProperty.AO_MAP, tex);
                builder.setTexCoord(MaterialProperty.AO_MAP, texCoord == null ? 0 : texCoord);
            }

            TextureModel mr = mat.getMetallicRoughnessTexture();
            if (mr != null) {
                Integer texCoord = mat.getMetallicRoughnessTexcoord();
                ResourceLocation tex = gltf.getImageData().getTextureModelToTextures().get(mr);
                builder.set(MaterialProperty.METALLIC_MAP, tex);
                builder.set(MaterialProperty.ROUGHNESS_MAP, tex);
                builder.setTexCoord(MaterialProperty.METALLIC_MAP, texCoord == null ? 0 : texCoord);
            }

            builder.set(MaterialProperty.AO, mat.getOcclusionStrength());

            float[] baseColorFactor = mat.getBaseColorFactor();
            Vector4f color;
            if (baseColorFactor == null) color = new Vector4f(1, 1, 1, 1);
            else color = new Vector4f(baseColorFactor[0], baseColorFactor[1], baseColorFactor[2], baseColorFactor[3]);
            builder.set(MaterialProperty.BASE_COLOR_FACTOR, color);

            float[] emissiveFactor = mat.getEmissiveFactor();
            Vector4f emissiveColor;
            if (emissiveFactor == null) emissiveColor = new Vector4f(0, 0, 0, 1);
            else emissiveColor = new Vector4f(emissiveFactor[0], emissiveFactor[1], emissiveFactor[2], 1);
            builder.set(MaterialProperty.EMISSIVE_FACTOR, emissiveColor);

            McMaterial material = builder.build(mat);
            materialData.addMaterial(material);
        }
    }
}