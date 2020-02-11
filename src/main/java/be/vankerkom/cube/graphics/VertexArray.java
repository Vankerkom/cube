package be.vankerkom.cube.graphics;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class VertexArray {

    private final List<Integer> buffers = new ArrayList<>();
    private int id;
    private int indicesCount;

    public VertexArray() {
        id = glGenVertexArrays();
    }

    public void bind() {
        glBindVertexArray(id);
    }

    public void addVertexBuffer(int size, FloatBuffer buffer) {
        int bufferId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(buffers.size(), size, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(buffers.size());
        buffers.add(bufferId);
    }

    public void addVertexBuffer(int size, float[] vertices) {
        final FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length);
        fb.put(vertices);
        fb.flip();
        addVertexBuffer(size, fb);
    }

    public void addIndexBuffer(IntBuffer buffer, int indicesCount) {
        int bufferId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        this.indicesCount = indicesCount;
        buffers.add(bufferId);
    }

    public void addIndexBuffer(int[] incides) {
        final IntBuffer ib = BufferUtils.createIntBuffer(incides.length);
        ib.put(incides);
        ib.flip();
        addIndexBuffer(ib, incides.length);
    }

    public void destroy() {
        glBindVertexArray(0);
        glDeleteVertexArrays(id);
        glDeleteBuffers(buffers.stream().mapToInt(i -> i).toArray());
        buffers.clear();
    }

    public void draw(int mode) {
        bind();
        glDrawElements(mode, indicesCount, GL_UNSIGNED_INT, 0);
    }
}
