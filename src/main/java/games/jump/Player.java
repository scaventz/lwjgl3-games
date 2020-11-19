package games.jump;

import engine.graph.Mesh;
import engine.obj.Actor;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * @author scaventz
 * @date 12/14/2020
 */
public class Player extends Actor {

    public Player(Vector3f position) {
        setPosition(position);
        // local coordinates
        float[] vertices = new float[]{
                -0.05f, 0.1f, 0.0f,  // top left
                position.x, position.y, 0.0f, // bottom left
                0.05f, position.y, 0.0f,  // bottom right
                0.05f, 0.1f, 0.0f};  // top right

        int[] indices = new int[]{0, 1, 3, 3, 1, 2};
        // texture
        float[] textureCoords = new float[]{
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
        };
        setMesh(new Mesh(vertices, indices, textureCoords));
        setTranslation(new Matrix4f());
        setVelocity(new Vector3f());
    }
}