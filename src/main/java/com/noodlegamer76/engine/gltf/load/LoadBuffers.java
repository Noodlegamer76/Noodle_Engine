package com.noodlegamer76.engine.gltf.load;

import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.geometry.GltfAccessorUtils;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.BufferViewModel;
import de.javagl.jgltf.model.ElementType;

import java.nio.*;

import static com.noodlegamer76.engine.gltf.geometry.GltfAccessorUtils.getNumComponents;

public class LoadBuffers {

    public static void loadBuffers(McGltf gltf) {
        for (AccessorModel accessor : gltf.getModel().getAccessorModels()) {
            BufferViewModel bufferView = accessor.getBufferViewModel();
            if (bufferView == null) continue;

            Buffer typed;

            switch (accessor.getComponentType()) {
                case 5126 -> { // FLOAT
                    float[] arr = GltfAccessorUtils.getFloatArray(accessor);
                    FloatBuffer fb = FloatBuffer.allocate(arr.length);
                    fb.put(arr).flip();
                    typed = fb;
                }
                case 5125 -> { // UNSIGNED_INT
                    int[] arr = GltfAccessorUtils.getIndexArray(accessor);
                    IntBuffer ib = IntBuffer.allocate(arr.length);
                    ib.put(arr).flip();
                    typed = ib;
                }
                case 5123, 5122, 5121, 5120 -> { // SHORT / BYTE / UNSIGNED variants
                    int[] arr;
                    if (accessor.getElementType() == ElementType.SCALAR && (accessor.getComponentType() == 5123 || accessor.getComponentType() == 5122)) {
                        arr = GltfAccessorUtils.getIndexArray(accessor);
                    } else {
                        arr = GltfAccessorUtils.getJointIndexArray(accessor);
                    }
                    IntBuffer ib = IntBuffer.allocate(arr.length);
                    ib.put(arr).flip();
                    typed = ib;
                }
                default -> throw new IllegalArgumentException("Unknown component type: " + accessor.getComponentType());
            }

            gltf.addAccessorBuffer(accessor, typed);
        }
    }
}
