#version 330

uniform mat4 projection;
uniform mat4 view;
uniform mat4 transform;

in vec3 position;
in vec2 textureCoordinates;

out vec2 uvCoordinates;

void main() {
    gl_Position = projection * view * transform * vec4(position, 1.0);
    uvCoordinates = textureCoordinates;
}
