package engine.graph;

import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.*;

/**
 * @author scaventz
 * @date 11/21/2020
 */
public class Mesh {
    private int vaoId;

    private int verVboId;

    private int idxVboId;

    private int textCoordsVboId;

    private int vertexCount;

    public Mesh(float[] vertices, int[] indices, float[] textureCoords) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            final FloatBuffer verticesBuffer = stack.mallocFloat(vertices.length);
            final IntBuffer indicesBuffer = stack.mallocInt(indices.length);

            // vao
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // vertices vbo
            vertexCount = indices.length;
            verticesBuffer.put(vertices).flip();
            verVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, verVboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_DYNAMIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.SIZE / 8 * 3, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            // indices vbo
            idxVboId = glGenBuffers();
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            // textureCoords vbo
            if (textureCoords != null) {
                final FloatBuffer textureCoordsBuffer = stack.mallocFloat(textureCoords.length);
                textureCoordsBuffer.put(textureCoords).flip();
                textCoordsVboId = glGenBuffers();
                glBindBuffer(GL_ARRAY_BUFFER, textCoordsVboId);
                glBufferData(GL_ARRAY_BUFFER, textureCoordsBuffer, GL_STATIC_DRAW);
                glVertexAttribPointer(1, 2, GL_FLOAT, false, Float.SIZE / 8 * 2, 0);
                glEnableVertexAttribArray(1);
                glBindBuffer(GL_ARRAY_BUFFER, 0);
            }

            glBindVertexArray(0);
        }
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(verVboId);
        glDeleteBuffers(idxVboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}