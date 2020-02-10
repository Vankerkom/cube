package be.vankerkom.cube;

public class Chunk {

    private static final int SIZE = 8;
    private static final int HEIGHT = 16;

    private byte[] blocks = new byte[SIZE * SIZE * HEIGHT];

    public Chunk() {
        int blockIndex = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < SIZE; x++) {
                for (int z = 0; z < SIZE; z++) {
                    blocks[blockIndex++] = (byte) (x == y ? 1 : 0);
                }
            }
        }
    }

    private static final int[] TOP_FACE = {1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1};
    private static final int[] BOTTOM_FACE = {0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1};
    private static final int[] FRONT_FACE = {1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1};
    private static final int[] RIGHT_FACE = {1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0};
    private static final int[] BACK_FACE = {0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0};
    private static final int[] LEFT_FACE = {0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1};

    public VertexArray createMesh() {
        final ChunkMeshBuilder meshBuilder = new ChunkMeshBuilder();

        int blockIndex = 0;
        for (int y = 0; y < HEIGHT; y++) {

            for (int z = 0; z < SIZE; z++) {

                for (int x = 0; x < SIZE; x++) {

                    // Get the current block.
                    final byte currentBlockType = blocks[blockIndex++];

                    if (currentBlockType == 0) { // Skip air blocks. // TODO Replace w/ a solid system.
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
        if (y < 0 || x < 0 || z < 0) {
            return true;
        }

        if (y >= HEIGHT || x >= SIZE || z >= SIZE) {
            return true;
        }

        final int blockIndex = getBlockIndex(x, y, z);
        final byte blockAtIndex = blocks[blockIndex];

        return blockAtIndex != currentBlockType;
    }

    private int getBlockIndex(int x, int y, int z) {
        return (SIZE * SIZE * y) + (z * SIZE) + x;
    }

}
