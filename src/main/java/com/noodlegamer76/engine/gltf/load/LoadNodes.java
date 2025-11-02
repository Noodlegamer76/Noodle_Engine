package com.noodlegamer76.engine.gltf.load;

import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.node.Node;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.NodeModel;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class LoadNodes {
    public static void loadNodes(McGltf gltf) {
        for (NodeModel nodeModel: gltf.getModel().getNodeModels()) {
            List<MeshModel> meshes = nodeModel.getMeshModels();
            List<MeshData> meshData = new ArrayList<>();

            meshes.forEach((meshModel -> meshData.add(gltf.getMeshModelToMeshData().get(meshModel))));

            float[] modelMatrixArr = new float[16];
            nodeModel.computeGlobalTransform(modelMatrixArr);
            Matrix4f modelMatrix = new Matrix4f().set(modelMatrixArr);

            Node node = new Node(modelMatrix, nodeModel);

            gltf.addNodeModelToNode(nodeModel, node);

            for (MeshData data: meshData) {
                gltf.addMeshToNode(data, node);
                data.setNode(node);
            }
        }

        for (NodeModel nodeModel: gltf.getModel().getNodeModels()) {
            Node node = gltf.getNodeModelToNode().get(nodeModel);
            Node parent = gltf.getNodeModelToNode().get(nodeModel.getParent());
            if (parent != null) {
                node.setParent(parent);
            }
        }
    }
}
