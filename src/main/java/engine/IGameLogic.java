package engine;

/**
 * @author scaventz
 * @date 11/20/2020
 */
public interface IGameLogic {

    void init() throws Exception;

    void input();

    void update(float interval);

    void render();

    void cleanup();
}