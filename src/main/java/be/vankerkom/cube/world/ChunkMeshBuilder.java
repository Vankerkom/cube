package be.vankerkom.cube.world;

import be.vankerkom.cube.graphics.VertexArray;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class ChunkMeshBuilder {

    private ArrayList<Float> vertices = new ArrayList<>();
    private ArrayList<Integer> incides = new ArrayList<>();
    private ArrayList<Float> uvCoordinates = new ArrayList<>();
    private ArrayList<Byte> lightLevels = new ArrayList<>();

    private int verticesCount = 0;

    public void addFace(int x, int y, int z, int[] faceData) {
        for (int i = 0; i < 12; i += 3) {
            vertices.add((float) x + faceData[i]);
            vertices.add((float) y + faceData[i + 1]);
            vertices.add((float) z + faceData[i + 2]);
        }

        int zero = verticesCount;
        int one = verticesCount + 1;
        int two = verticesCount + 2;
        int three = verticesCount + 3;

        verticesCount += 4;

        // 0, 1, 2, 2, 3, 0
        incides.add(zero);
        incides.add(one);
        incides.add(two);
        incides.add(two);
        incides.add(three);
        incides.add(zero);

        uvCoordinates.add(0.0F);
        uvCoordinates.add(0.0F);

        uvCoordinates.add(1.0F);
        uvCoordinates.add(0.0F);

        uvCoordinates.add(1.0F);
        uvCoordinates.add(1.0F);

        uvCoordinates.add(0.0F);
        uvCoordinates.add(1.0F);

        // The last int is the light level.
        final byte lightLevel = (byte) (faceData[12] & 0xF);
        lightLevels.add(lightLevel);
        lightLevels.add(lightLevel);
        lightLevels.add(lightLevel);
        lightLevels.add(lightLevel);
    }

    public VertexArray build() {
        // This is not efficient but #prototype.
        final FloatBuffer vertBuffer = BufferUtils.createFloatBuffer(vertices.size());
        for (float vertex : vertices) {
            vertBuffer.put(vertex);
        }

        final IntBuffer incidesBuffer = BufferUtils.createIntBuffer(incides.size());
        for (Integer incide : incides) {
            incidesBuffer.put(incide);
        }

        final FloatBuffer uvBuffer = BufferUtils.createFloatBuffer(uvCoordinates.size());
        for (float uv : uvCoordinates) {
            uvBuffer.put(uv);
        }

        final ByteBuffer lightBuffer = BufferUtils.createByteBuffer(lightLevels.size());
        for (byte lightLevel : lightLevels) {
            lightBuffer.put(lightLevel);
        }

        vertBuffer.flip();
        incidesBuffer.flip();
        uvBuffer.flip();
        lightBuffer.flip();

        // Generate a mesh per block.
        final VertexArray vao = new VertexArray();
        vao.bind();
        vao.addVertexBuffer(3, vertBuffer);
        vao.addVertexBuffer(2, uvBuffer);
        vao.addVertexBuffer(1, lightBuffer);
        vao.addIndexBuffer(incidesBuffer, incides.size());

        return vao;
    }

}
