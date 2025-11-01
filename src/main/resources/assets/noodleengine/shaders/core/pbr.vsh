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
in vec4 JointIndices;
in vec4 JointWeights;

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
    mat4 modelView = ModelViewMat[BaseInstance + gl_InstanceID];

    int index = (BaseInstance + gl_InstanceID) * 2;
    //for some reason i have to multiply the uvs by 15 or it doesn't work correctly
    ivec2 light = ivec2(LightUvData[index] * 15, LightUvData[index + 1] * 15);

    vec4 pos = vec4(Position, 1.0);
    gl_Position = ProjMat * modelView * pos;

    texCoord0 = UV0;
    normalCoord = NormalUV;
    metallicCoord = MetallicUV;
    roughnessCoord = RoughnessUV;
    aoCoord = AoUV;
    emissiveCoord = EmissiveUV;

    lightColor = minecraft_sample_lightmap(lightTex, light);
    vertexColor = Color;
    fragNormal = normalize(Normal);
    fragViewPos = (modelView * pos).xyz;
    fragWorldPos = pos.xyz;
}
