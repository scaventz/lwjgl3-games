package engine.obj;

import engine.graph.Mesh;

/**
 * @author scaventz
 * @date 12/20/2020
 */
public class Terrain extends GameObject {

    private float altitude = 0.0f;

    public Terrain() {
        new Terrain(0f);
    }

    public Terrain(float attitude) {
        this.altitude = attitude;

        float[] vertices = new float[]{
                -1.0f, altitude, 0.0f,  // top left
                -1.0f, -1.0f, 0.0f, // bottom left
                1.0f, -1.0f, 0.0f,  // bottom right
                1.0f, altitude, 0.0f};  // top right
        int[] indices = new int[]{0, 1, 3, 3, 1, 2};
        float[] textureCoords = new float[]{
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f};

        setMesh(new Mesh(vertices, indices, textureCoords));
    }

    public float getAltitude() {
        return altitude;
    }
}