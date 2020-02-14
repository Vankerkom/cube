#version 330 core

out vec4 color;

in vec2 uvCoordinates;
in float faceLightLevel;

uniform sampler2D textureSampler;

void main() {
    color = faceLightLevel * texture(textureSampler, uvCoordinates);
}
