package games.jump.tool;

import engine.graph.Mesh;
import engine.graph.ShaderProgram;
import engine.graph.Texture;
import engine.util.Utils;
import games.jump.obj.Barrier;
import org.joml.Matrix4f;

import java.time.Instant;
import java.util.Random;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

/**
 * @author scaventz
 * @date 12/18/2020
 */
public class RandomShape2D {

    private static Random random = new Random((long) 0.5);
    private static ShaderProgram shaderProgram;
    private static Texture texture;

    static {
        /*shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource());
        shaderProgram.createFragmentShader(Utils.loadResource());
        shaderProgram.link();*/
        shaderProgram = ShaderProgram.create("game/jump/shader/barrier.vs","game/jump/shader/barrier.fs");

        shaderProgram.bind();
        shaderProgram.setUniform("model", new Matrix4f());
        glUniform1i(glGetUniformLocation(shaderProgram.getProgramId(), "barrierTexture"), 0);
        shaderProgram.unbind();

        texture = new Texture("game/jump/texture/barrier.jpg");
    }

    public static Barrier next() {
        try {
            final Instant start = Instant.now();
            float[] vertices = new float[]{
                    0.0f, random.nextFloat() * 0.02f, 0.0f,  // top
                    -random.nextFloat() * 0.05f, -0.1f, 0.0f, // left
                    random.nextFloat() * 0.05f, -0.1f, 0.0f   // right
            };
            int[] indices = {0, 1, 2};

            Barrier barrier = new Barrier();
            barrier.setMesh(new Mesh(vertices, indices, null));
            barrier.setTexture(texture);
            barrier.setShaderProgram(shaderProgram);
            barrier.setTranslation(new Matrix4f().translate(1, 0, 0));
            final Instant end = Instant.now();
            return barrier;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}