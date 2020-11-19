package games.jump;

import engine.obj.GameObject;
import engine.IGameLogic;
import engine.Window;
import engine.graph.Texture;
import engine.graph.TextureFactory;
import engine.obj.Terrain;
import games.jump.obj.Barrier;
import games.jump.tool.RandomShape2D;
import org.joml.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author scaventz
 * @date 11/20/2020
 */
@Component
public class JumpGame implements IGameLogic {

    private Player player;

    private Terrain terrain;

    private List<GameObject> objList = new ArrayList<>(16);

    @Resource
    private Window window;

    @Resource
    private Renderer renderer;

    @Override
    public void init() throws Exception {
        renderer.init();

        /*******************************/
        /*********** terrain ************/
        /*******************************/
        terrain = new Terrain(-0.1f);
        Texture terrainTexture = TextureFactory.create("game/jump/texture/terrain.png");
        terrain.setTexture(terrainTexture);
        terrain.setShaderProgram(renderer.getTerrainShaderProgram());

        /*******************************/
        /*********** player ************/
        /*******************************/
        player = new Player(new Vector3f(0, terrain.getAltitude(), 0));
        Texture playerTexture = TextureFactory.create("game/jump/texture/you.png");
        player.setTexture(playerTexture);
        player.setShaderProgram(renderer.getPlayerShaderProgram());

        objList.add(player);
        objList.add(terrain);
    }

    @Override
    public void input() {
        // a player can't move in horizontal when he is in air
        if (!player.isInAir()) {
            if (window.isKeyPressed(GLFW_KEY_A)) {
                player.getVelocity().set(-1, 0, 0);
            } else if (window.isKeyPressed(GLFW_KEY_D)) {
                player.getVelocity().set(1, 0, 0);
            } else {
                player.getVelocity().x = 0;
            }

            if (window.isKeyPressed(GLFW_KEY_SPACE)) {
                player.setInAir(true);
                player.getVelocity().add(0, 2.5f, 0);
            }
        }
    }

    @Override
    public void update(float interval) {
        if (player.getVelocity().length() > 0) {
            // horizontal translation = horizontal speed * interval
            float x = player.getVelocity().x * interval;
            // vertical translation = vertical speed * interval
            float y = player.getVelocity().y * interval;
            player.getVelocity().y -= 10 * interval;

            player.getTranslation().translate(x, y, 0);
            player.getPosition().add(x, y, 0);
            if (player.getPosition().y <= terrain.getAltitude()) {
                player.getVelocity().y = 0;
                player.setInAir(false);
            }
            renderer.update(player);
        }

        final Random random = new Random();
        if (random.nextInt(1000) > 997) {
            Barrier barrier = RandomShape2D.next();
            objList.add(barrier);
        }

        for (GameObject barrier : objList) {
            if (barrier instanceof Barrier) {
                barrier.getTranslation().translate(new Vector3f(-1, 0, 0).mul(interval));
                renderer.update(barrier);
                if (barrier.getTranslation().m03() < -1) {
                    System.out.println("removed");
                    objList.remove(barrier);
                }
            }
        }

    }

    @Override
    public void render() {
        renderer.render(objList);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        player.getMesh().cleanUp();
    }
}