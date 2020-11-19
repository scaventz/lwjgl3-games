import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author scaventz
 * @date 12/18/2020
 */
public class Main {


    public static void main(String[] args) throws URISyntaxException, IOException {
        Resource resource = new ClassPathResource("game/jump/texture/you.png");
        System.out.println(resource.getURI());
        System.out.println(resource.getURL());
        System.out.println(resource.getFile().getAbsolutePath());
    }
}