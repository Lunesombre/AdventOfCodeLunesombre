package adventofcode.aoc2024;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AdventOfCodeApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(AdventOfCodeApplication.class, args);

    System.out.println("Ok, bye ! 🫶");
    shutdown(context);
  }

  private static void shutdown(ConfigurableApplicationContext context) {
    SpringApplication.exit(context, () -> 0);
  }

}
