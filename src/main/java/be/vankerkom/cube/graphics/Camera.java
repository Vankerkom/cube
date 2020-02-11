package be.vankerkom.cube.graphics;

import be.vankerkom.cube.input.Keyboard;
import be.vankerkom.cube.input.Mouse;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {

    public final Matrix4f viewMatrix;
    public final Vector3f position;
    public final Vector2f rotation;

    private final Vector3f up = new Vector3f();
    private final Vector3f forward = new Vector3f();
    private final Vector3f right = new Vector3f();

    public Camera(final Vector3f position) {
        this.viewMatrix = new Matrix4f().identity();
        this.position = new Vector3f(position);
        this.rotation = new Vector2f(0, 0);
        calculateMatrix();
    }

    public void update(double delta) {
        final float movementSpeed = 5.0F * (float) delta;

        // TODO Optimize this
        viewMatrix.positiveY(up).mul(movementSpeed);
        viewMatrix.positiveZ(forward).negate().mul(movementSpeed);
        viewMatrix.positiveX(right).mul(movementSpeed);
        forward.y = 0; // Only allow movement in X & Z plane.

        if (Keyboard.isDown(GLFW.GLFW_KEY_W)) {
            position.add(forward);
        }

        if (Keyboard.isDown(GLFW.GLFW_KEY_S)) {
            position.sub(forward);
        }

        if (Keyboard.isDown(GLFW.GLFW_KEY_D)) {
            position.add(right);
        }

        if (Keyboard.isDown(GLFW.GLFW_KEY_A)) {
            position.sub(right);
        }

        if (Keyboard.isDown(GLFW.GLFW_KEY_SPACE)) {
            position.add(up);
        }

        if (Keyboard.isDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            position.sub(up);
        }

        // TODO Refactor and rework the rotation.
        rotation.y += Mouse.MOUSE_DELTA.x * 0.01F;
        rotation.x += Mouse.MOUSE_DELTA.y * 0.01F;

        if (position.x != 0 || position.y != 0 || position.z != 0 || Mouse.MOUSE_DELTA.x != 0 || Mouse.MOUSE_DELTA.y != 0) {
            calculateMatrix();
        }
    }

    private void calculateMatrix() {
        viewMatrix
                .identity()
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .translate(-position.x, -position.y, -position.z);
    }

}
