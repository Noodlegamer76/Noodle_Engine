package com.noodlegamer76.engine.gltf.load;

import com.noodlegamer76.engine.gltf.McGltf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ModelStorage {
    private static Map<ResourceLocation, McGltf> MODELS = new HashMap<>();

    public static void addModel(ResourceLocation location, McGltf model) {
        MODELS.put(location, model);
    }

    public static McGltf getModel(ResourceLocation location) {
        return MODELS.get(location);
    }

    public static Map<ResourceLocation, McGltf> getModels() {
        return MODELS;
    }
}
