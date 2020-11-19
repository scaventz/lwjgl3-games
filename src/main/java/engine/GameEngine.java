package engine;

import org.lwjgl.opengl.GL33C;
import org.lwjgl.system.MemoryStack;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20C.GL_MAX_VERTEX_ATTRIBS;

/**
 * @author scaventz
 * @date 11/20/2020
 */
@Component
public class GameEngine {

    public static final int TARGET_FPS = 300;
    public static final int TARGET_UPS = 300;

    @Resource
    private Window window;

    @Resource
    private Timer timer;

    @Resource
    private IGameLogic gameLogic;

    public void run() {
        try {
            init();
            exec();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }

    protected void init() {
        window.init();
        timer.init();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            final IntBuffer intBuffer = stack.mallocInt(1);
            GL33C.glGetIntegerv(GL_MAX_VERTEX_ATTRIBS, intBuffer);
            System.out.println("Maximum nr of vertex attributes supported: " + intBuffer.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exec() throws Exception {
        gameLogic.init();
        gameLoop();
    }

    protected void gameLoop() {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if (!window.isvSync()) {
                sync();
            }
        }
    }

    protected void cleanup() {
        gameLogic.cleanup();
    }

    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
            }
        }
    }

    protected void input() {
        gameLogic.input();
    }

    protected void update(float interval) {
        gameLogic.update(interval);
    }

    protected void render() {
        gameLogic.render();
        window.update();
    }
}