package com.noodlegamer76.engine.gltf.node;

import de.javagl.jgltf.model.NodeModel;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class Node {
    private Node parent;
    private Matrix4f global;
    private Matrix4f local;
    private final NodeModel nodeModel;

    public Node(Matrix4f global, Matrix4f local, NodeModel nodeModel) {
        this.global = global;
        this.local = local;
        this.nodeModel = nodeModel;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setGlobal(Matrix4f global) {
        this.global = global;
    }

    public Vector3f getPosition() {
        return new Vector3f(global.m30(), global.m31(), global.m32());
    }

    public Quaternionf getRotation() {
        return new Quaternionf(global.m00(), global.m01(), global.m02(), global.m03());
    }

    public Vector3f getScale() {
        return new Vector3f(global.m00(), global.m01(), global.m02());
    }

    @Nullable
    public Node getParent() {
        return parent;
    }

    public Matrix4f getGlobal() {
        return global;
    }

    public NodeModel getNodeModel() {
        return nodeModel;
    }

    public Matrix4f getLocal() {
        return local;
    }
}