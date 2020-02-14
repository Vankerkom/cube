package be.vankerkom.cube;

import be.vankerkom.cube.graphics.Camera;
import be.vankerkom.cube.graphics.Shader;
import be.vankerkom.cube.graphics.Texture;
import be.vankerkom.cube.input.Keyboard;
import be.vankerkom.cube.input.Mouse;
import be.vankerkom.cube.world.ChunkManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import javax.xml.soap.Text;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class CubeMain {

    private long window;
    private Matrix4f projectionMatrix = new Matrix4f().identity();
    private int width = 1280;
    private int height = 720;
    private boolean wireframe = false;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(width, height, "Cube", NULL, NULL);

        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        centerMouse();

        glfwSetKeyCallback(window, Keyboard::keyPressCallback);
        glfwSetCursorPosCallback(window, Mouse::mouseCursorPosCallback);
        glfwSetCursorEnterCallback(window, Mouse::mouseCursorEnterCallback);

        calculateProjection();

        glfwSetFramebufferSizeCallback(window, (window, frameWidth, frameHeight) -> {
            if (frameWidth > 0 && frameHeight > 0) {
                System.out.println("Framebuffer Resize: " + frameWidth + "x" + frameHeight);
                this.width = frameWidth;
                this.height = frameHeight;
                calculateProjection();
                glViewport(0, 0, width, height);
            }
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
    }

    private void calculateProjection() {
        float fovy = (float) Math.toRadians(65.0F);
        float aspectRatio = (float) width / height;

        projectionMatrix.identity()
                .perspective(fovy, aspectRatio, 0.1F, 100.0F);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(135.0f / 255, 206.0F / 255, 250.0F / 255, 0.0f);

        final Texture texture = new Texture("test.png");

        texture.bind();

        final ChunkManager chunkManager = new ChunkManager();

        final Shader shader = new Shader("static", "static");

        shader.bind();

        final Camera camera = new Camera(new Vector3f(0.0F, 10.0F, 0.0F));

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        // Make the window visible
        glfwShowWindow(window);


        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        double currentTime;
        double previousTime = glfwGetTime();
        double delta;

        // Center the cursor on start.
        setMouseVisible(false);

        while (!glfwWindowShouldClose(window)) {
            currentTime = glfwGetTime();
            delta = currentTime - previousTime;
            previousTime = currentTime;

            if (Keyboard.isPressed(GLFW_KEY_ESCAPE)) {
                setMouseVisible(!Mouse.VISIBLE);
            }

            if (Keyboard.isPressed(GLFW_KEY_P)) {
                wireframe = !wireframe;
                glPolygonMode(GL_FRONT_AND_BACK, wireframe ? GL_LINE : GL_FILL);
            }

            camera.update(delta);
            chunkManager.update();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            shader.setUniform("projection", projectionMatrix);
            shader.setUniform("view", camera.viewMatrix);

            chunkManager.draw(shader);

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be invoked during this call.
            Keyboard.update();
            centerMouse();

            glfwPollEvents();
        }

        chunkManager.destroy();
        texture.destroy();
        shader.destroy();
    }

    private void setMouseVisible(boolean visible) {
        Mouse.VISIBLE = visible;
        final int cursor = (visible) ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_HIDDEN;
        glfwSetInputMode(window, GLFW_CURSOR, cursor);
    }

    void centerMouse() {
        Mouse.setPosition(width / 2.0, height / 2.0);

        if (!Mouse.VISIBLE) {
            glfwSetCursorPos(window, (double) width / 2, (double) height / 2);
        }
    }

    public static void main(String[] args) {
        new CubeMain().run();
    }

}
