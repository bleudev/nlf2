#version 330

uniform sampler2D InSampler;

in vec2 texCoord;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

layout(std140) uniform RainbowConfig {
    float Hue;
    float Strength;
};

const vec3 Gray = vec3(0.3, 0.59, 0.11);

out vec4 fragColor;

vec3 hueToRgb(float hue) {
    hue = fract(hue);
    vec3 rgb = abs(mod(hue * 6.0 + vec3(0.0, 4.0, 2.0), 6.0) - 3.0) - 1.0;
    return clamp(rgb, 0.0, 1.0);
}

void main() {
    vec2 oneTexel = 1.0 / InSize;
    vec4 InTexel = texture(InSampler, texCoord);

    vec3 pure = hueToRgb(Hue);

    float RedValue = dot(InTexel.rgb, Gray * pure.r);
    float GreenValue = dot(InTexel.rgb, Gray * pure.g);
    float BlueValue = dot(InTexel.rgb, Gray * pure.b);
    vec3 OutColor = vec3(RedValue, GreenValue, BlueValue);

    float Luma = dot(OutColor, Gray);
    vec3 Chroma = OutColor - Luma;
    OutColor = (Chroma * 2.4) + Luma;

    float strength = Strength;
    fragColor = vec4(mix(InTexel.rgb, OutColor, strength), 1.0);
}
