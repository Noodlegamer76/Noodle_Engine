package com.noodlegamer76.engine.gltf.load;

import com.mojang.blaze3d.platform.NativeImage;
import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.gltf.McGltf;
import de.javagl.jgltf.model.ImageModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class LoadImages {

    public static void loadImages(McGltf gltf) {
        ImageData imageData = gltf.getImageData();
        Map<ImageModel, ResourceLocation> uploaded = new HashMap<>();

        for (int i = 0; i < gltf.getModel().getImageModels().size(); i++) {
            ImageModel model = gltf.getModel().getImageModels().get(i);

            if (uploaded.containsKey(model)) continue;

            ByteBuffer buffer = model.getImageData();
            if (buffer == null) continue;

            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);

            try {
                NativeImage image = loadImage(bytes);
                if (image == null) {
                    NoodleEngine.LOGGER.error("Failed to load image: " + getImageName(model, i));
                    continue;
                }

                imageData.addImage(image);

                String name = getImageName(model, i);

                DynamicTexture dynamicTexture = new DynamicTexture(image);
                TextureManager manager = Minecraft.getInstance().getTextureManager();
                ResourceLocation location = gltf.getLocation().withSuffix("/" + name);
                manager.register(location, dynamicTexture);

                imageData.addTexture(dynamicTexture);
                imageData.addTextureLocation(location, dynamicTexture);
                imageData.addImageLocation(model, location);

                uploaded.put(model, location);

            } catch (Exception e) {
                NoodleEngine.LOGGER.error("Could not load image: " + model.getName(), e);
            }
        }
    }

    private static NativeImage loadImage(byte[] bytes) {
        try {
            return NativeImage.read(new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            NoodleEngine.LOGGER.info("PNG loading failed, trying STB image loader");
        }

        ByteBuffer buffer = null;
        try {
            buffer = MemoryUtil.memAlloc(bytes.length);
            buffer.put(bytes);
            buffer.flip();

            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);

                ByteBuffer imageBuffer = STBImage.stbi_load_from_memory(buffer, width, height, channels, 4);

                if (imageBuffer == null) {
                    NoodleEngine.LOGGER.error("STB Image loading failed: " + STBImage.stbi_failure_reason());
                    return null;
                }

                try {
                    int w = width.get(0);
                    int h = height.get(0);

                    NativeImage image = new NativeImage(NativeImage.Format.RGBA, w, h, false);

                    for (int y = 0; y < h; y++) {
                        for (int x = 0; x < w; x++) {
                            int i = (y * w + x) * 4;
                            int r = imageBuffer.get(i) & 0xFF;
                            int g = imageBuffer.get(i + 1) & 0xFF;
                            int b = imageBuffer.get(i + 2) & 0xFF;
                            int a = imageBuffer.get(i + 3) & 0xFF;

                            int color = (a << 24) | (b << 16) | (g << 8) | r;
                            image.setPixelRGBA(x, y, color);
                        }
                    }

                    return image;
                } finally {
                    STBImage.stbi_image_free(imageBuffer);
                }
            }
        } catch (Exception e) {
            NoodleEngine.LOGGER.error("Error loading image with STB", e);
            return null;
        } finally {
            if (buffer != null) {
                MemoryUtil.memFree(buffer);
            }
        }
    }

    private static String getImageName(ImageModel image, int fallbackIndex) {
        String name = image.getName();
        if (name != null && !name.isEmpty()) return name;

        name = image.getUri();
        if (name != null && !name.isEmpty()) {
            int lastSlash = name.lastIndexOf('/');
            if (lastSlash >= 0) name = name.substring(lastSlash + 1);
            int dot = name.lastIndexOf('.');
            if (dot >= 0) name = name.substring(0, dot);
            return name;
        }

        return "texture_" + fallbackIndex;
    }
}
