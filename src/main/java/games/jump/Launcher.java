package games.jump;

import engine.GameEngine;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.Resource;

@SpringBootApplication
@PropertySource(value = {"classpath:game/jump/config/window.properties"})
@ComponentScan(basePackages = {"engine", "games.jump"})
public class Launcher implements ApplicationRunner {

    @Resource
    private GameEngine engine;

    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            engine.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
