package com.noodlegamer76.engine.gltf.node;

import de.javagl.jgltf.model.NodeModel;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class Node {
    private Node parent;
    private Matrix4f modelMatrix;

    public Node(Matrix4f modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setModelMatrix(Matrix4f modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public Vector3f getPosition() {
        return new Vector3f(modelMatrix.m30(), modelMatrix.m31(), modelMatrix.m32());
    }

    public Quaternionf getRotation() {
        return new Quaternionf(modelMatrix.m00(), modelMatrix.m01(), modelMatrix.m02(), modelMatrix.m03());
    }

    public Vector3f getScale() {
        return new Vector3f(modelMatrix.m00(), modelMatrix.m01(), modelMatrix.m02());
    }

    @Nullable
    public Node getParent() {
        return parent;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }
}