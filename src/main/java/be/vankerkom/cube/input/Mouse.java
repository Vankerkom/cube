package be.vankerkom.cube.input;

import org.joml.Vector2d;

public class Mouse {

    public static boolean VISIBLE = false;
    public static final Vector2d MOUSE_DELTA = new Vector2d();
    public static final Vector2d MOUSE_POSITION = new Vector2d();

    public static void mouseCursorPosCallback(long window, double xPosition, double yPosition) {
        MOUSE_DELTA.set(xPosition - MOUSE_POSITION.x, yPosition - MOUSE_POSITION.y);
        MOUSE_POSITION.set(xPosition, yPosition);
        // System.out.println("Mouse Position >> " + MOUSE_POSITION.x + ", " + MOUSE_POSITION.y);
        // System.out.println("Mouse Delta >> " + MOUSE_DELTA.x + ", " + MOUSE_DELTA.y);
    }

    public static void mouseCursorEnterCallback(long window, boolean entered) {
        System.out.println("mouseCursorEnterCallback >> " + entered);
    }

    public static void setPosition(double xPosition, double yPosition) {
        MOUSE_DELTA.set(0, 0);
        MOUSE_POSITION.set(xPosition, yPosition);
    }
}
