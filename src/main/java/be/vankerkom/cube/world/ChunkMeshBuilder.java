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

    public void addFace(int x, int y, int z, int[] faceData) {
        int zero = vertices.size() / 3;
        vertices.add((float) x + faceData[0]);
        vertices.add((float) y + faceData[1]);
        vertices.add((float) z + faceData[2]);

        int one = vertices.size() / 3;
        vertices.add((float) x + faceData[3]);
        vertices.add((float) y + faceData[4]);
        vertices.add((float) z + faceData[5]);

        int two = vertices.size() / 3;
        vertices.add((float) x + faceData[6]);
        vertices.add((float) y + faceData[7]);
        vertices.add((float) z + faceData[8]);

        int three = vertices.size() / 3;
        vertices.add((float) x + faceData[9]);
        vertices.add((float) y + faceData[10]);
        vertices.add((float) z + faceData[11]);

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

        lightLevels.add((byte)5);
        lightLevels.add((byte)5);
        lightLevels.add((byte)5);
        lightLevels.add((byte)5);
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
