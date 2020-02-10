package be.vankerkom.cube.world;

import be.vankerkom.cube.Shader;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class ChunkManager {

    private List<Chunk> chunkList = new ArrayList<>();

    public ChunkManager() {
        for (int x = 0; x < 4; x++) {
            for (int z = 0; z < 4; z++) {
                final Chunk newChunk = new Chunk(new Vector2i(x ,z));
                chunkList.add(newChunk);
            }
        }
    }

    public void draw(Shader shader) {
        chunkList.forEach(chunk -> chunk.draw(shader));
    }

    public void destroy() {
        chunkList.forEach(Chunk::destroy);
    }

}
