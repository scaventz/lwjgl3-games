package engine.graph;

import java.util.HashMap;
import java.util.Map;

/**
 * @author scaventz
 * @date 12/19/2020
 */
public class TextureFactory {

    private static Map<String, Texture> textures = new HashMap<>();

    public static Texture create(String path) {
        Texture cache = textures.get(path);
        if (cache != null) {
            System.out.println("cached: " + path);
            return cache;
        }

        Texture texture = new Texture(path);
        textures.put(path,texture);
        return texture;
    }
}