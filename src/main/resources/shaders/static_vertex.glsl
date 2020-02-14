#version 330

uniform mat4 projection;
uniform mat4 view;
uniform mat4 transform;

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCoordinates;
layout(location = 2) in float lightLevel;

out vec2 uvCoordinates;
out float faceLightLevel;

void main() {
    gl_Position = projection * view * transform * vec4(position, 1.0);
    uvCoordinates = textureCoordinates;
    faceLightLevel = lightLevel / 5.0f;
}
