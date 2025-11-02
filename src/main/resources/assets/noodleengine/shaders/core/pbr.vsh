#version 430 core
#moj_import <light.glsl>

in vec3 Position;
in vec4 Color;
in vec3 Normal;
in vec2 UV0;
in vec2 NormalUV;
in vec2 MetallicUV;
in vec2 RoughnessUV;
in vec2 AoUV;
in vec2 EmissiveUV;
in vec4 JointWeights;
in ivec4 JointIndices;

uniform mat4 ProjMat;
uniform vec4 baseColorFactor;
uniform sampler2D lightTex;
uniform int BaseInstance;

layout(std430, binding = 0) buffer ModelViewBlock {
    mat4 ModelViewMat[];
};
layout(std430, binding = 1) buffer LightUvBlock {
    int LightUvData[];
};
layout(std430, binding = 2) buffer JointMatBlock {
    mat4 JointMatrices[];
};
layout(std430, binding = 3) buffer JointOffsetBlock {
    int InstanceOffsets[];
};

out vec2 texCoord0;
out vec2 normalCoord;
out vec2 metallicCoord;
out vec2 roughnessCoord;
out vec2 aoCoord;
out vec2 emissiveCoord;
out vec3 fragViewPos;
out vec3 fragWorldPos;
out vec3 fragNormal;
out vec4 vertexColor;
out vec4 lightColor;

void main() {
    int instanceIndex = BaseInstance + gl_InstanceID;
    mat4 modelView = ModelViewMat[instanceIndex];

    int lightIndex = instanceIndex * 2;
    ivec2 light = ivec2(LightUvData[lightIndex] * 15, LightUvData[lightIndex + 1] * 15);

    int jointBase = InstanceOffsets[instanceIndex];

    float weightSum = JointWeights.x + JointWeights.y + JointWeights.z + JointWeights.w;
    int jointSum = JointIndices.x + JointIndices.y + JointIndices.z + JointIndices.w;
    float uvSum = NormalUV.x + NormalUV.y;
    float uv0Sum = UV0.x + UV0.y;

        mat4 skinMat;
        if (weightSum >= -100 || weightSum <= 100) {
            skinMat = mat4(0.0);
        } else {
            skinMat = mat4(1.0);
        }

  vec4 pos = skinMat * vec4(Position, 1.0);
  gl_Position = ProjMat * modelView * pos;

    texCoord0 = UV0;
    normalCoord = NormalUV;
    metallicCoord = MetallicUV;
    roughnessCoord = RoughnessUV;
    aoCoord = AoUV;
    emissiveCoord = EmissiveUV;

    lightColor = minecraft_sample_lightmap(lightTex, light);
    vertexColor = Color;

  fragNormal = normalize(mat3(modelView) * mat3(skinMat) * Normal);
    fragViewPos = (modelView * pos).xyz;
    fragWorldPos = pos.xyz;
}
