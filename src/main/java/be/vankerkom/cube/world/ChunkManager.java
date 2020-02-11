package be.vankerkom.cube.world;

import be.vankerkom.cube.graphics.Shader;
import org.joml.Vector2i;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class ChunkManager {

    private Map<Vector2i, Chunk> chunkList = new LinkedHashMap<>();
    private Queue<Chunk> updateRequired = new LinkedList<>();

    private long totalChunkUpdates = 0;

    public ChunkManager() {
        for (int x = -2; x < 2; x++) {
            for (int z = -2; z < 2; z++) {
                Vector2i position = new Vector2i(x, z);
                final Chunk newChunk = new Chunk(this, position);
                chunkList.put(position, newChunk);
            }
        }

        updateRequired.addAll(chunkList.values());
    }

    public byte getBlockAt(final int x, int y, int z) {
        final Vector2i chunkPosition = new Vector2i(x / Chunk.SIZE, z / Chunk.SIZE);
        final Chunk chunk = chunkList.get(chunkPosition);
        return chunk != null
                ? chunk.getBlockAt(x & 0xF, y, z & 0xF)
                : (byte) 0; // Default to air.
    }

    public void draw(Shader shader) {
        chunkList.values().forEach(chunk -> chunk.draw(shader));
    }

    public void update() {
        if (!updateRequired.isEmpty()) {
            while (!updateRequired.isEmpty()) {
                updateRequired.poll().generateMesh();
                totalChunkUpdates++;
            }

            System.out.println("Chunk updates >> " + totalChunkUpdates);
        }
    }

    public void destroy() {
        updateRequired.clear();
        chunkList.values().forEach(Chunk::destroy);
        chunkList.clear();
    }

}
