package learning.demobank_2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoBank2Application {

    public static void main(String[] args) {
        SpringApplication.run(DemoBank2Application.class, args);
    }

}
