package engine.obj;

import org.joml.Vector3f;

/**
 * @author scaventz
 * @date 12/20/2020
 */
public class Actor extends GameObject {

    private Vector3f position;
    private Vector3f velocity;

    private boolean isInAir;

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public boolean isInAir() {
        return isInAir;
    }

    public void setInAir(boolean inAir) {
        isInAir = inAir;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
}