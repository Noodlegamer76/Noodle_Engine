package com.noodlegamer76.engine.gltf;

import com.noodlegamer76.engine.gltf.animation.animation.AnimationClip;
import com.noodlegamer76.engine.gltf.animation.skins.LoadSkins;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.load.*;
import com.noodlegamer76.engine.gltf.node.Node;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class McGltf {
    private final ResourceLocation location;
    private final DefaultGltfModel model;
    private final Map<AccessorModel, Buffer> accessorBuffers = new HashMap<>();
    private final ImageData imageData = new ImageData();
    private final MaterialData materialData = new MaterialData();
    private final List<MeshData> meshes = new ArrayList<>();
    private final Map<MeshModel, MeshData> meshModelToMeshData = new HashMap<>();
    private final Map<MeshData, Node> meshToNode = new HashMap<>();
    private final Map<NodeModel, Node> nodeModelToNode = new HashMap<>();
    private final Map<SkinModel, Map<Node, Matrix4f>> inverseBindMatrices = new HashMap<>();
    private final Map<String, AnimationClip> animations = new HashMap<>();

    public McGltf(DefaultGltfModel model, ResourceLocation location) {
        this.model = model;
        this.location = location;
    }

    public void setup() {
        LoadBuffers.loadBuffers(this);
        LoadImages.loadImages(this);
        LoadTextures.loadTextures(this);
        LoadMaterials.loadMaterials(this);
        LoadMeshes.loadMeshes(this);
        LoadNodes.loadNodes(this);
        LoadSkins.loadSkins(this);
        LoadAnimations.loadAnimations(this);
    }

    public void close() {
        meshes.forEach(MeshData::close);
    }

    public DefaultGltfModel getModel() {
        return model;
    }

    public ImageData getImageData() {
        return imageData;
    }

    public Map<AccessorModel, Buffer> getAccessorBuffers() {
        return accessorBuffers;
    }

    public void addAccessorBuffer(AccessorModel accessorModel, Buffer buffer) {
        accessorBuffers.put(accessorModel, buffer);
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public MaterialData getMaterialData() {
        return materialData;
    }

    public List<MeshData> getMeshes() {
        return meshes;
    }

    public void addMesh(MeshData mesh) {
        meshes.add(mesh);
        meshModelToMeshData.put(mesh.getMeshModel(), mesh);
    }

    public Map<MeshData, Node> getMeshToNode() {
        return meshToNode;
    }

    public void addMeshToNode(MeshData mesh, Node node) {
        meshToNode.put(mesh, node);
    }

    public Map<MeshModel, MeshData> getMeshModelToMeshData() {
        return meshModelToMeshData;
    }

    public Map<NodeModel, Node> getNodeModelToNode() {
        return nodeModelToNode;
    }

    public void addNodeModelToNode(NodeModel nodeModel, Node node) {
        nodeModelToNode.put(nodeModel, node);
    }

    public Map<String, AnimationClip> getAnimations() {
        return animations;
    }

    public Map<SkinModel, Map<Node, Matrix4f>> getInverseBindMatrices() {
        return inverseBindMatrices;
    }
}
