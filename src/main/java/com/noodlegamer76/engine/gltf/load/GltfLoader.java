package com.noodlegamer76.engine.gltf.load;

import com.mojang.blaze3d.systems.RenderSystem;
import com.noodlegamer76.engine.gltf.McGltf;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.io.GltfModelReader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.Map;

public class GltfLoader {
    private static final GltfModelReader READER = new GltfModelReader();

    public static void loadAllGlbModels(ResourceManager resourceManager, String folder) {
        Map<ResourceLocation, Resource> locations = resourceManager.listResources(folder, path -> path.toString().endsWith(".glb"));
        for (Map.Entry<ResourceLocation, Resource> resource : locations.entrySet()) {
            loadModel(resource, resourceManager);
        }
    }

    @Nullable
    public static void loadModel(Map.Entry<ResourceLocation, Resource> resource, ResourceManager resourceManager) {
        try (InputStream resourceStream = resource.getValue().open()) {

            DefaultGltfModel gltfModel = (DefaultGltfModel) READER.readWithoutReferences(resourceStream);

            McGltf model = new McGltf(gltfModel, resource.getKey());

            RenderSystem.recordRenderCall(model::setup);

            ModelStorage.addModel(resource.getKey(), model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
