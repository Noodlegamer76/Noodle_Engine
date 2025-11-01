package com.noodlegamer76.engine.gltf.load;

import com.mojang.blaze3d.platform.NativeImage;
import de.javagl.jgltf.model.ImageModel;
import de.javagl.jgltf.model.TextureModel;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageData {
    private final List<NativeImage> images = new ArrayList<>();
    private final Map<ImageModel, ResourceLocation> imageLocations = new HashMap<>();
    private final Map<ResourceLocation, DynamicTexture> textureLocations = new HashMap<>();
    private final List<DynamicTexture> textures = new ArrayList<>();
    private final Map<TextureModel, ResourceLocation> TextureModelToTextures = new HashMap<>();

    public void addImage(NativeImage image) {
        images.add(image);
    }

    public Map<TextureModel, ResourceLocation> getTextureModelToTextures() {
        return TextureModelToTextures;
    }

    public void addTextureModelToTextures(TextureModel textureModel, ResourceLocation location) {
        TextureModelToTextures.put(textureModel, location);
    }

    public void addImageLocation(ImageModel imageModel, ResourceLocation location) {
        imageLocations.put(imageModel, location);
    }

    public void addTextureLocation(ResourceLocation location, DynamicTexture texture) {
        textureLocations.put(location, texture);
    }

    public void addTexture(DynamicTexture texture) {
        textures.add(texture);
    }

    public List<DynamicTexture> getTextures() {
        return textures;
    }

    public List<NativeImage> getImages() {
        return images;
    }

    public Map<ImageModel, ResourceLocation> getImageLocations() {
        return imageLocations;
    }

    public Map<ResourceLocation, DynamicTexture> getTextureLocations() {
        return textureLocations;
    }
}
