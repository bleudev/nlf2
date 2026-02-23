#version 330

uniform sampler2D InSampler;

in vec2 texCoord;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

layout(std140) uniform ChmajConfig {
    vec3 ChMatrix;
};

out vec4 fragColor;

void main() {
    vec3 col = texture(InSampler, texCoord).rgb;
    float colSum = col.r + col.g + col.b;
    float newSum = col.r * ChMatrix.r + col.g * ChMatrix.g + col.b * ChMatrix.b;
    if (colSum > newSum * 2) {
        float avgCol = colSum * 0.3333;
        fragColor = vec4(avgCol, avgCol, avgCol, 1.0);
    } else {
        fragColor = vec4(col, 1.0);
    }
}