#version 140

uniform mat4 projection;
uniform mat4 view;
uniform mat4 transform;

in vec3 position;

void main() {
    gl_Position = projection * view * transform * vec4(position, 1.0);
}
