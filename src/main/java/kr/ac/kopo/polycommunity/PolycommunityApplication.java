package kr.ac.kopo.polycommunity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PolycommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(PolycommunityApplication.class, args);
    }

}
