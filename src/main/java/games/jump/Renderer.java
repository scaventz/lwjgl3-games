package games.jump;

import engine.obj.GameObject;
import engine.Window;
import engine.graph.ShaderProgram;
import engine.util.Utils;
import org.joml.Matrix4f;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL30.*;

@Component
public class Renderer {

    private ShaderProgram playerShaderProgram;
    private ShaderProgram terrainShaderProgram;

    @Resource
    private Window window;

    public Renderer() {
    }

    public void init() {
        playerShaderProgram = ShaderProgram.create("game/jump/shader/player.vs", "game/jump/shader/player.fs");
        playerShaderProgram.bind();
        playerShaderProgram.setUniform("model", new Matrix4f());
        playerShaderProgram.setUniform("view", new Matrix4f());
        float ratio = window.getWidth() / window.getHeight();
        playerShaderProgram.setUniform("projection", new Matrix4f().setOrtho2D(-ratio, ratio, -1, 1));
        glUniform1i(glGetUniformLocation(playerShaderProgram.getProgramId(), "playerTexture"), 0);
        playerShaderProgram.unbind();

        terrainShaderProgram = ShaderProgram.create("game/jump/shader/terrain.vs", "game/jump/shader/terrain.fs");
        terrainShaderProgram.bind();
        terrainShaderProgram.setUniform("projection", new Matrix4f().setOrtho2D(-ratio, ratio, -1, 1));
        glUniform1i(glGetUniformLocation(terrainShaderProgram.getProgramId(), "terrainTexture"), 0);
        terrainShaderProgram.unbind();

        glDepthMask(false);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void update(GameObject gameObj) {
        gameObj.getShaderProgram().bind();
        gameObj.getShaderProgram().setUniform("model", gameObj.getTranslation());
        gameObj.getShaderProgram().unbind();
    }

    public void render(List<GameObject> objs) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        for (GameObject obj : objs) {
            // Bind to the VAO
            glBindVertexArray(obj.getMesh().getVaoId());
            // glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            // Draw the mesh
            obj.getShaderProgram().bind();
            // activate the texture unit first before binding texture
            // After activating a texture unit, a subsequent glBindTexture call will bind that texture to the currently active texture unit.
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, obj.getTexture().getId());
            glDrawElements(GL_TRIANGLES, obj.getMesh().getVertexCount(), GL_UNSIGNED_INT, 0);
            obj.getShaderProgram().unbind();
            // Restore state
            glBindVertexArray(0);
        }
    }

    public void clear() {
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        if (playerShaderProgram != null) {
            playerShaderProgram.cleanup();
        }
    }

    public ShaderProgram getPlayerShaderProgram() {
        return playerShaderProgram;
    }

    public ShaderProgram getTerrainShaderProgram() {
        return terrainShaderProgram;
    }
}
