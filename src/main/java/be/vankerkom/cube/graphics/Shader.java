package be.vankerkom.cube.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int id;

    public Shader(String vertexName, String fragmentName) {
        id = glCreateProgram();
        compile(vertexName, fragmentName);
    }

    private void compile(String vertexName, String fragmentName) {
        final String vertexShaderFile = vertexName.concat("_vertex.glsl");
        final String fragmentShaderFile = fragmentName.concat("_fragment.glsl");

        int vertexShaderId = compileShader(GL_VERTEX_SHADER, vertexShaderFile);
        int fragmentShaderId = compileShader(GL_FRAGMENT_SHADER, fragmentShaderFile);

        glAttachShader(id, vertexShaderId);
        glAttachShader(id, fragmentShaderId);
        glLinkProgram(id);
        glValidateProgram(id);

        final int linkStatus = glGetProgrami(id, GL_LINK_STATUS);
        if (linkStatus != GL_TRUE) {
            System.err.println("Failed to link shader: " + vertexName + ", " + fragmentName);
            glDeleteProgram(id);
            id = 0;
        }

        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);
    }

    private int compileShader(final int shaderType, final String shaderFile) {
        final String shaderSource = loadShaderFromResourceOrEmpty(shaderFile);
        if (shaderSource == null || shaderSource.isEmpty()) {
            return 0;
        }

        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            return 0;
        }

        glShaderSource(shaderId, shaderSource);
        glCompileShader(shaderId);

        final int compileStatus = glGetShaderi(shaderId, GL_COMPILE_STATUS);
        if (compileStatus != GL_TRUE) {
            System.err.println("Failed to compile Shader: " + shaderFile + '\n' + glGetShaderInfoLog(shaderId));
            glDeleteShader(shaderId);
            shaderId = 0;
        }

        return shaderId;
    }

    public void bind() {
        glUseProgram(id);
    }

    public void destroy() {
        if (id > 0) {
            glDeleteProgram(id);
            id = 0;
        }
    }

    public void setUniform(final String uniformName, Matrix4f matrix) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        matrix.get(fb);
        glUniformMatrix4fv(getUniformLocation(uniformName), false, fb);
    }

    public void setUniform(final String uniformName, Vector3f vector) {
        glUniform3f(getUniformLocation(uniformName), vector.x, vector.y, vector.z);
    }

    public void setUniform(final String uniformName, Vector2f vector) {
        glUniform2f(getUniformLocation(uniformName), vector.x, vector.y);
    }

    private int getUniformLocation(final String uniformName) {
        int uniformId = glGetUniformLocation(id, uniformName);

        if (uniformId == -1) {
            System.err.println("Could not locate uniform: " + uniformName);
        }

        return uniformId;
    }

    private static String loadShaderFromResourceOrEmpty(String resourceLocation) {
        String result;

        try {
            result = loadShaderFromResource(resourceLocation);
        } catch (IOException ignored) {
            System.err.println("Failed to load shader " + resourceLocation + " from resource.");
            result = "";
        }

        return result;
    }

    private static String loadShaderFromResource(String resourceLocation) throws IOException {

        StringBuilder result = new StringBuilder();
        String currentLine;

        InputStream inputStream = Class.class.getResourceAsStream("/shaders/" + resourceLocation);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        while ((currentLine = reader.readLine()) != null) {
            result.append(currentLine).append('\n');
        }

        reader.close();

        return result.toString();

    }

}
