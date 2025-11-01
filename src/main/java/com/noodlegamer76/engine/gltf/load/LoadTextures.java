package com.noodlegamer76.engine.gltf.load;

import com.noodlegamer76.engine.gltf.McGltf;
import de.javagl.jgltf.model.TextureModel;
import net.minecraft.resources.ResourceLocation;

public class LoadTextures {
    public static void loadTextures(McGltf gltf) {
        ImageData data = gltf.getImageData();
        for (TextureModel texture : gltf.getModel().getTextureModels()) {
            ResourceLocation location = data.getImageLocations().get(texture.getImageModel());
            if (location == null) continue;
            data.addTextureModelToTextures(texture, location);
        }
    }
}
