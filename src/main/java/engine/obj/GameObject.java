package engine.obj;

import engine.graph.Mesh;
import engine.graph.ShaderProgram;
import engine.graph.Texture;
import org.joml.Matrix4f;

/**
 * @author scaventz
 * @date 12/12/2020
 */
public class GameObject {
    private Mesh mesh;
    private Texture texture;
    private ShaderProgram shaderProgram;

    private Matrix4f translation;

    public GameObject() {
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    public void setShaderProgram(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public Matrix4f getTranslation() {
        return translation;
    }

    public void setTranslation(Matrix4f translation) {
        this.translation = translation;
    }
}