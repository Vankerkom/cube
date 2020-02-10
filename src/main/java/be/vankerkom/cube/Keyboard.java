package be.vankerkom.cube;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public final class Keyboard {

    private static boolean[] IS_DOWN = new boolean[GLFW_KEY_LAST];
    private static boolean[] IS_PRESSED = new boolean[GLFW_KEY_LAST];
    private static boolean[] IS_RELEASED = new boolean[GLFW_KEY_LAST];

    public static void keyPressCallback(long window, int key, int scancode, int action, int mods) {
        if (key < 0) {
            return;
        }

        if (action == GLFW_PRESS) {
            IS_DOWN[key] = true;
            IS_PRESSED[key] = true;
        } else if (action == GLFW_RELEASE) {
            IS_DOWN[key] = false;
            IS_RELEASED[key] = true;
        }
    }

    public static boolean isDown(final int keyIndex) {
        return IS_DOWN[keyIndex];
    }

    public static boolean isPressed(final int keyIndex) {
        return IS_PRESSED[keyIndex] && IS_DOWN[keyIndex];
    }

    public static boolean isReleased(final int keyIndex) {
        return IS_RELEASED[keyIndex];
    }

    public static void update() {
        Arrays.fill(IS_PRESSED, false);
        Arrays.fill(IS_RELEASED, false);
    }

}
