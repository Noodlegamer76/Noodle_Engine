#version 430 core

uniform sampler2D albedoMap;
uniform sampler2D normalMap;
uniform sampler2D metallicMap;
uniform sampler2D roughnessMap;
uniform sampler2D aoMap;
uniform sampler2D emissiveMap;

uniform float metallic;
uniform float roughness;
uniform float ao;
uniform vec4 baseColorFactor;
uniform vec4 emissiveFactor;
uniform vec3 cameraPos;
uniform int hasAlbedo;

// Hardcoded directional light
const vec3 lightDir = normalize(vec3(0.15, 1.0, 0.1));

in vec2 texCoord0;
in vec2 normalCoord;
in vec2 metallicCoord;
in vec2 roughnessCoord;
in vec2 aoCoord;
in vec2 emissiveCoord;
in vec3 fragViewPos;
in vec3 fragWorldPos;
in vec3 fragNormal;
in vec4 vertexColor;
in vec4 lightColor;

out vec4 fragColor;

void main() {
    vec3 albedo;
    if (hasAlbedo > 0) {
        albedo = texture(albedoMap, texCoord0).rgb * baseColorFactor.rgb * vertexColor.rgb;
    } else {
        albedo = vertexColor.rgb * baseColorFactor.rgb;
    }

    float m = clamp(metallic * texture(metallicMap, metallicCoord).r, 0.0, 1.0);
    float r = clamp(roughness * texture(roughnessMap, roughnessCoord).r, 0.04, 1.0);

    float aoVal = clamp(ao * texture(aoMap, aoCoord).r, 0.0, 1.0);
    float aoFactor = mix(1.0, aoVal, 0.5);

    vec3 N = normalize(fragNormal);
    vec3 L = normalize(lightDir);
    float NdotL = max(dot(N, L), 0.0);

    // diffuse
    vec3 globalDiffuse = albedo * lightColor.xyz;
    vec3 dirDiffuse = albedo * NdotL * aoFactor;
    vec3 diffuse = globalDiffuse + dirDiffuse;

    // simple specular
    vec3 R = reflect(-L, N);
    float specPower = mix(8.0, 64.0, 1.0 - r);
    float specFactor = pow(max(dot(R, L), 0.0), specPower);
    vec3 specular = lightColor.xyz * m * 0.5 * specFactor;

    // ambient
    vec3 ambient = albedo * 0.05 * aoVal;

    // emissive
    vec3 emissive = texture(emissiveMap, emissiveCoord).rgb * emissiveFactor.rgb;

    vec3 color = ambient + diffuse + specular + emissive;
    fragColor = vec4(color, baseColorFactor.a * vertexColor.a);
}
