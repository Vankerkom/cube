#version 330 core

out vec4 color;

in vec2 uvCoordinates;

uniform sampler2D textureSampler;

void main() {
    color = texture(textureSampler, uvCoordinates);
}
