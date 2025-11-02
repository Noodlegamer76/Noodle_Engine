package com.noodlegamer76.engine.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

public class ModVertexFormat {
    public static final VertexFormatElement ELEMENT_NORMAL_UV    = VertexFormatElement.register(VertexFormatElement.findNextId(),  3, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 2);
    public static final VertexFormatElement ELEMENT_METALLIC_UV  = VertexFormatElement.register(VertexFormatElement.findNextId(),  4, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 2);
    public static final VertexFormatElement ELEMENT_ROUGHNESS_UV = VertexFormatElement.register(VertexFormatElement.findNextId(),  5, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 2);
    public static final VertexFormatElement ELEMENT_AO_UV        = VertexFormatElement.register(VertexFormatElement.findNextId(),  6, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 2);
    public static final VertexFormatElement ELEMENT_EMISSIVE_UV  = VertexFormatElement.register(VertexFormatElement.findNextId(), 7, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 2);
    public static final VertexFormatElement JOINTS               = VertexFormatElement.register(VertexFormatElement.findNextId(), 8, VertexFormatElement.Type.INT,   VertexFormatElement.Usage.UV, 4);
    public static final VertexFormatElement WEIGHTS              = VertexFormatElement.register(VertexFormatElement.findNextId(), 9, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 4);

    public static final VertexFormat GLB_PBR = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("Color", VertexFormatElement.COLOR)
            .add("UV0", VertexFormatElement.UV0)
            .add("Normal", VertexFormatElement.NORMAL)

            .add("NormalUV", ELEMENT_NORMAL_UV)
            .add("MetallicUV", ELEMENT_METALLIC_UV)
            .add("RoughnessUV", ELEMENT_ROUGHNESS_UV)
            .add("AoUV", ELEMENT_AO_UV)
            .add("EmissiveUV", ELEMENT_EMISSIVE_UV)
            .add("JointIndices", JOINTS)
            .add("JointWeights", WEIGHTS)
            .build();
}
