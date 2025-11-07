package com.noodlegamer76.engine.core.component;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.engine.client.renderer.gltf.GlbRenderer;
import com.noodlegamer76.engine.client.renderer.gltf.RenderableMesh;
import com.noodlegamer76.engine.client.renderer.gltf.RenderableMeshes;
import com.noodlegamer76.engine.core.network.GameObjectSerializers;
import com.noodlegamer76.engine.core.network.SyncedVar;
import com.noodlegamer76.engine.entity.GameObject;
import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.load.ModelStorage;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class GltfModelComponent extends Component {
    private final SyncedVar<ResourceLocation> modelLocation = new SyncedVar<>(this, ResourceLocation.withDefaultNamespace(""), GameObjectSerializers.RESOURCE_LOCATION);
    private final List<RenderableMesh> meshes = new ArrayList<>();

    public GltfModelComponent(GameObject gameObject, ResourceLocation modelLocation) {
        super(InitComponents.GLTF_MODEL, gameObject);
        this.modelLocation.setValue(modelLocation, true);
    }

    public GltfModelComponent(GameObject gameObject) {
        super(InitComponents.GLTF_MODEL, gameObject);
    }

    @Override
    public void onAdded(Level level) {
        if (level.isClientSide) {
            updateModel();
        }
    }

    @Override
    public void onUpdated(Level level) {
        if (level.isClientSide) {
            updateModel();
        }
    }

    public void updateModel() {
        McGltf model = ModelStorage.getModel(modelLocation.getValue());
        if (model == null) {
            return;
        }
        else {
            for (RenderableMesh mesh : meshes) {
                RenderableMeshes.remove(mesh);
            }
        }
        for (MeshData meshData : model.getMeshes()) {

            PoseStack poseStack = new PoseStack();
            poseStack.pushPose();

            Vector3f translation = gameObject.getPosition(Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true)).toVector3f();
            Vector3f scale = new Vector3f(gameObject.getScale());
            poseStack.mulPose(new Matrix4f().translation(translation));
            poseStack.mulPose(new Matrix4f().scale(scale));

            RenderableMesh mesh = GlbRenderer.addInstance(meshData, poseStack, -1);
            meshes.add(mesh);

            poseStack.popPose();
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putString("modelLocation", modelLocation.getValue().toString());
    }

    @Override
    public void loadAdditional(CompoundTag tag) {
        modelLocation.setValue(ResourceLocation.parse(tag.getString("modelLocation")), true);
    }

    @Override
    public void onRemoved(Level level) {
        for (RenderableMesh mesh : meshes) {
            RenderableMeshes.remove(mesh);
        }
    }

    public void setModelLocation(ResourceLocation modelLocation) {
        this.modelLocation.setValue(modelLocation, true);
    }

    public ResourceLocation getModelLocation() {
        return modelLocation.getValue();
    }

    public List<RenderableMesh> getMeshes() {
        return meshes;
    }

    @Override
    public List<SyncedVar<?>> getSyncedData() {
        return List.of(
                modelLocation
        );
    }
}
