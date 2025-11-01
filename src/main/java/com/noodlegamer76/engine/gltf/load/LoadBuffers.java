package com.noodlegamer76.engine.gltf.load;

import com.noodlegamer76.engine.gltf.McGltf;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.BufferModel;
import de.javagl.jgltf.model.BufferViewModel;

import java.nio.*;

public class LoadBuffers {

    public static void loadBuffers(McGltf gltf) {
        for (AccessorModel accessor : gltf.getModel().getAccessorModels()) {
            BufferViewModel bufferView = accessor.getBufferViewModel();
            if (bufferView == null) continue;

            BufferModel buffer = bufferView.getBufferModel();
            if (buffer == null) continue;

            ByteBuffer data = buffer.getBufferData();
            int offset = bufferView.getByteOffset() + accessor.getByteOffset();

            int stride = accessor.getByteStride();
            if (stride <= 0) {
                stride = accessor.getElementType().getNumComponents() *
                        getBytesPerComponent(accessor.getComponentType());
            }

            int length = stride * accessor.getCount();
            ByteBuffer slice = data.duplicate();
            slice.position(offset);
            slice.limit(offset + length);
            slice = slice.slice();

            Buffer typed = getTypedBuffer(slice, accessor.getComponentType());
            gltf.addAccessorBuffer(accessor, typed);
        }
    }

    private static int getBytesPerComponent(int glType) {
        return switch (glType) {
            case 5120, 5121 -> 1; // BYTE, UNSIGNED_BYTE
            case 5122, 5123 -> 2; // SHORT, UNSIGNED_SHORT
            case 5125, 5126 -> 4; // UNSIGNED_INT, FLOAT
            default -> throw new IllegalArgumentException("Unknown component type: " + glType);
        };
    }

    private static Buffer getTypedBuffer(ByteBuffer slice, int glType) {
        return switch (glType) {
            case 5120 -> slice; // BYTE
            case 5121 -> slice; // UNSIGNED_BYTE
            case 5122, 5123 -> slice.asShortBuffer();
            case 5125 -> slice.asIntBuffer();
            case 5126 -> slice.asFloatBuffer();
            default -> throw new IllegalArgumentException("Unknown component type: " + glType);
        };
    }
}
