package be.vankerkom.cube.world;

import be.vankerkom.cube.graphics.Shader;
import be.vankerkom.cube.graphics.VertexArray;
import org.joml.Matrix4f;
import org.joml.Vector2i;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class Chunk {

    public static final int SIZE = 16;
    public static final int HEIGHT = 8;

    private final ChunkManager chunkManager;
    private final Vector2i position;
    private final Matrix4f transformMatrix = new Matrix4f();
    private byte[] blocks = new byte[SIZE * SIZE * HEIGHT];
    private VertexArray mesh;

    public Chunk(ChunkManager chunkManager, Vector2i position) {
        this.chunkManager = chunkManager;
        this.position = position;
        int blockIndex = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < SIZE; x++) {
                for (int z = 0; z < SIZE; z++) {
                    blocks[blockIndex++] = (byte) 1;
                }
            }
        }

        this.transformMatrix.setTranslation((float) (this.position.x * SIZE), 0, (float) (this.position.y * SIZE));
    }

    private static final int[] TOP_FACE = {1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 5};
    private static final int[] BOTTOM_FACE = {0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 2};
    private static final int[] FRONT_FACE = {1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 4};
    private static final int[] RIGHT_FACE = {1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 3};
    private static final int[] BACK_FACE = {0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 3};
    private static final int[] LEFT_FACE = {0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 3};

    private VertexArray createMesh() {
        final ChunkMeshBuilder meshBuilder = new ChunkMeshBuilder();

        int blockIndex = 0;
        for (int y = 0; y < HEIGHT; y++) {

            for (int z = 0; z < SIZE; z++) {

                for (int x = 0; x < SIZE; x++) {

                    // Get the current block.
                    final byte currentBlockType = blocks[blockIndex++];

                    if (currentBlockType == 0) { // Skip air blocks.
                        continue;
                    }

                    if (isNotSolid(currentBlockType, x, y + 1, z)) {
                        meshBuilder.addFace(x, y, z, TOP_FACE);
                    }

                    if (isNotSolid(currentBlockType, x, y - 1, z)) {
                        meshBuilder.addFace(x, y, z, BOTTOM_FACE);
                    }

                    if (isNotSolid(currentBlockType, x, y, z + 1)) {
                        meshBuilder.addFace(x, y, z, FRONT_FACE);
                    }

                    if (isNotSolid(currentBlockType, x + 1, y, z)) {
                        meshBuilder.addFace(x, y, z, RIGHT_FACE);
                    }

                    if (isNotSolid(currentBlockType, x, y, z - 1)) {
                        meshBuilder.addFace(x, y, z, BACK_FACE);
                    }

                    if (isNotSolid(currentBlockType, x - 1, y, z)) {
                        meshBuilder.addFace(x, y, z, LEFT_FACE);
                    }

                }

            }

        }

        return meshBuilder.build();
    }

    private boolean isNotSolid(byte currentBlockType, int x, int y, int z) {
        if (y < 0 || y >= HEIGHT) {
            return true;
        }

        byte otherBlockType;

        if (x < 0 || z < 0 || x >= SIZE || z >= SIZE) { // Ask neighbours
            final int worldX = position.x * SIZE + x;
            final int worldZ = position.y * SIZE + z;
            otherBlockType = chunkManager.getBlockAt(worldX, y, worldZ);
        } else {
            final int blockIndex = getBlockIndex(x, y, z);
            otherBlockType = blocks[blockIndex];
        }

        return otherBlockType == 0 || otherBlockType != currentBlockType;
    }

    private int getBlockIndex(int x, int y, int z) {
        return (SIZE * SIZE * y) + (z * SIZE) + x;
    }

    public byte getBlockAt(int x, int y, int z) {
        return blocks[getBlockIndex(x, y, z)];
    }

    public void draw(Shader shader) {
        if (mesh == null) {
            return;
        }

        shader.setUniform("transform", this.transformMatrix);
        mesh.draw(GL_TRIANGLES);
    }

    public void destroy() {
        if (mesh != null) {
            mesh.destroy();
        }
    }

    public void generateMesh() {
        this.mesh = createMesh();
    }
}
