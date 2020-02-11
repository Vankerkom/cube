package be.vankerkom.cube.graphics;

public final class VertexArrayFactory {

    public static VertexArray createPlane() {
        final VertexArray vao = new VertexArray();
        vao.bind();

        float[] vertices = {
                // Left bottom triangle
                -0.5F, -0.5F, 0.0F,
                0.5F, -0.5F, 0.0F,
                0.5F, 0.5F, 0.0F,
                -0.5F, 0.5F, 0.0F,
        };

        int[] indices = {
                0, 1, 2, 2, 3, 0
        };

        vao.addVertexBuffer(3, vertices);
        vao.addIndexBuffer(indices);
        return vao;
    }

    public static VertexArray createCube(float x, float y, float z) {
        final VertexArray vao = new VertexArray();
        vao.bind();

        float[] vertices = {
                // front
                0.0F + x, 0.0F + y, 1.0F + z,
                1.0F + x, 0.0F + y, 1.0F + z,
                1.0F + x, 1.0F + y, 1.0F + z,
                0.0F + x, 1.0F + y, 1.0F + z,
                // back
                0.0F + x, 0.0F + y, 0.0F + z,
                1.0F + x, 0.0F + y, 0.0F + z,
                1.0F + x, 1.0F + y, 0.0F + z,
                0.0F + x, 1.0F + y, 0.0F + z
        };

        int[] indices = {
                // front
                0, 1, 2,
                2, 3, 0,
                // right
                1, 5, 6,
                6, 2, 1,
                // back
                7, 6, 5,
                5, 4, 7,
                // left
                4, 0, 3,
                3, 7, 4,
                // bottom
                4, 5, 1,
                1, 0, 4,
                // top
                3, 2, 6,
                6, 7, 3
        };

        vao.addVertexBuffer(3, vertices);
        vao.addIndexBuffer(indices);
        return vao;
    }

    public static VertexArray createCube() {
        return createCube(0, 0, 0);
    }

}
