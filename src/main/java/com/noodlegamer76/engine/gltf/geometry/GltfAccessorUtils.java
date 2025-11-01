package com.noodlegamer76.engine.gltf.geometry;

import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.ElementType;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GltfAccessorUtils {

    public static float[] getFloatArray(AccessorModel accessor) {
        if (accessor == null) return null;

        int count = accessor.getCount();
        int numComponents = getNumComponents(accessor.getElementType());
        float[] out = new float[count * numComponents];

        BufferViewModel view = accessor.getBufferViewModel();
        if (view == null) return null;

        ByteBuffer buffer = view.getBufferViewData().duplicate().order(ByteOrder.LITTLE_ENDIAN);
        int accessorOffset = accessor.getByteOffset();
        int elementSize = numComponents * getComponentSizeBytes(accessor.getComponentType());
        int stride = view.getByteStride() != null && view.getByteStride() > 0 ? view.getByteStride() : elementSize;

        if (accessor.getComponentType() == 5126) {
            for (int i = 0; i < count; i++) {
                int pos = accessorOffset + i * stride;
                buffer.position(pos);
                for (int c = 0; c < numComponents; c++) {
                    out[i * numComponents + c] = buffer.getFloat();
                }
            }
            return out;
        }

        boolean normalized = accessor.isNormalized();
        for (int i = 0; i < count; i++) {
            int pos = accessorOffset + i * stride;
            buffer.position(pos);
            for (int c = 0; c < numComponents; c++) {
                int idx = i * numComponents + c;
                switch (accessor.getComponentType()) {
                    case 5120 -> out[idx] = normalized ? buffer.get() / (float) Byte.MAX_VALUE : buffer.get(); // BYTE
                    case 5121 -> out[idx] = normalized ? (buffer.get() & 0xFF) / 255f : buffer.get() & 0xFF; // UNSIGNED_BYTE
                    case 5122 -> out[idx] = normalized ? buffer.getShort() / (float) Short.MAX_VALUE : buffer.getShort(); // SHORT
                    case 5123 -> out[idx] = normalized ? (buffer.getShort() & 0xFFFF) / 65535f : buffer.getShort() & 0xFFFF; // UNSIGNED_SHORT
                    case 5125 -> out[idx] = buffer.getInt(); // UNSIGNED_INT
                    default -> throw new IllegalArgumentException("Unsupported componentType: " + accessor.getComponentType());
                }
            }
        }
        return out;
    }

    public static int[] getIndexArray(AccessorModel accessor) {
        if (accessor == null) return null;

        int count = accessor.getCount();
        int[] out = new int[count];

        BufferViewModel view = accessor.getBufferViewModel();
        if (view == null) return null;

        ByteBuffer buffer = view.getBufferViewData().duplicate().order(ByteOrder.LITTLE_ENDIAN);
        int accessorOffset = accessor.getByteOffset();
        int elementSize = getComponentSizeBytes(accessor.getComponentType());
        int stride = view.getByteStride() != null && view.getByteStride() > 0 ? view.getByteStride() : elementSize;

        switch (accessor.getComponentType()) {
            case 5121 -> { // UNSIGNED_BYTE
                for (int i = 0; i < count; i++) {
                    buffer.position(accessorOffset + i * stride);
                    out[i] = buffer.get() & 0xFF;
                }
            }
            case 5123 -> { // UNSIGNED_SHORT
                for (int i = 0; i < count; i++) {
                    buffer.position(accessorOffset + i * stride);
                    out[i] = buffer.getShort() & 0xFFFF;
                }
            }
            case 5125 -> { // UNSIGNED_INT
                for (int i = 0; i < count; i++) {
                    buffer.position(accessorOffset + i * stride);
                    out[i] = buffer.getInt();
                }
            }
            default -> throw new IllegalArgumentException("Unsupported index component type: " + accessor.getComponentType());
        }
        return out;
    }

    public static int[] getJointIndexArray(AccessorModel accessor) {
        if (accessor == null) return null;

        int numComponents = getNumComponents(accessor.getElementType());
        int count = accessor.getCount() * numComponents;
        int[] out = new int[count];

        BufferViewModel view = accessor.getBufferViewModel();
        ByteBuffer buffer = view.getBufferViewData().duplicate().order(ByteOrder.LITTLE_ENDIAN);
        int accessorOffset = accessor.getByteOffset();
        int stride = view.getByteStride() != null ? view.getByteStride() : numComponents * getComponentSizeBytes(accessor.getComponentType());

        switch (accessor.getComponentType()) {
            case 5121 -> { // UNSIGNED_BYTE
                for (int i = 0; i < accessor.getCount(); i++) {
                    buffer.position(accessorOffset + i * stride);
                    for (int c = 0; c < numComponents; c++) {
                        out[i * numComponents + c] = buffer.get() & 0xFF;
                    }
                }
            }
            case 5123 -> { // UNSIGNED_SHORT
                for (int i = 0; i < accessor.getCount(); i++) {
                    buffer.position(accessorOffset + i * stride);
                    for (int c = 0; c < numComponents; c++) {
                        out[i * numComponents + c] = buffer.getShort() & 0xFFFF;
                    }
                }
            }
            default -> throw new IllegalArgumentException("Unsupported joint component type: " + accessor.getComponentType());
        }
        return out;
    }

    public static int getNumComponents(ElementType type) {
        return switch (type) {
            case SCALAR -> 1;
            case VEC2 -> 2;
            case VEC3 -> 3;
            case VEC4, MAT2 -> 4;
            case MAT3 -> 9;
            case MAT4 -> 16;
        };
    }

    public static int getComponentSizeBytes(int componentType) {
        return switch (componentType) {
            case 5120, 5121 -> 1; // BYTE, UNSIGNED_BYTE
            case 5122, 5123 -> 2; // SHORT, UNSIGNED_SHORT
            case 5125, 5126 -> 4; // UNSIGNED_INT, FLOAT
            default -> throw new IllegalArgumentException("Unknown componentType: " + componentType);
        };
    }
}