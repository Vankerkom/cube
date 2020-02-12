package be.vankerkom.cube.graphics;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
public class Texture {

    private static final int BYTES_PER_PIXEL = 4;

    private int id;
    private String fileName;
    private int width;
    private int height;

    public Texture(String fileName) {
        ByteBuffer imageBuffer = null;

        try {
            imageBuffer = getImageBuffer(loadTextureFromResource(fileName));
        } catch (IOException e) {
            System.err.println(e.toString());
            return;
        }

        this.id = glGenTextures();
        System.out.println("Texture id: " + id);

        bind();

        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private BufferedImage loadTextureFromResource(String resourceLocation) throws IOException {
        InputStream inputStream = Class.class.getResourceAsStream("/textures/" + resourceLocation);
        System.out.println("Loading texture: " + resourceLocation);
        return ImageIO.read(inputStream);
    }


    private ByteBuffer getImageBuffer(BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));      // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));       // Green component
                buffer.put((byte) (pixel & 0xFF));              // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));      // Alpha component. Only for RGBA
            }
        }

        buffer.flip();

        return buffer;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.id);
    }

    public void destroy() {
        if (id > 0) {
            glDeleteTextures(id);
            id = 0;
        }
    }

}
