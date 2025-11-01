package com.noodlegamer76.engine.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

public class ModVertexFormat {
    public static final VertexFormatElement ELEMENT_NORMAL_UV = new VertexFormatElement(0, 0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 2);
    public static final VertexFormatElement ELEMENT_METALLIC_UV = new VertexFormatElement(0, 1, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 2);
    public static final VertexFormatElement ELEMENT_ROUGHNESS_UV = new VertexFormatElement(0, 2, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 2);
    public static final VertexFormatElement ELEMENT_AO_UV = new VertexFormatElement(0, 3, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 2);
    public static final VertexFormatElement ELEMENT_EMISSIVE_UV = new VertexFormatElement(5, 4, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 2);
    public static final VertexFormatElement JOINTS = new VertexFormatElement(0, 6, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 4);
    public static final VertexFormatElement WEIGHTS = new VertexFormatElement(0, 7, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 4);


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