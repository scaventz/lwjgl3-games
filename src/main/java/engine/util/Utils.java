package engine.util;

import java.io.InputStream;
import java.util.Scanner;

/**
 * @author scaventz
 * @date 11/20/2020
 */
public class Utils {
    public static String loadResource(String fileName) {
        String result;
        try (InputStream in = Utils.class.getResourceAsStream("/" + fileName);
             Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}