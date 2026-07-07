#version 330

uniform sampler2D InSampler;

in vec2 texCoord;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

layout(std140) uniform CopyConfig {
    vec2 Size;
    float Strength;
};

out vec4 fragColor;

void main() {
    vec3 colA = texture(InSampler, texCoord).rgb;
    vec3 colB = texture(InSampler, texCoord * Size).rgb;
    float s = Strength;
    fragColor = vec4(mix(colA, colB, s), 1.0);
}