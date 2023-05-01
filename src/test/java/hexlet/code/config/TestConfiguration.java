package hexlet.code.config;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "hexlet.code")
@PropertySource(value = "classpath:application.yaml")
public class TestConfiguration {
    @Bean
    public Faker faker() {
        return new Faker();
    }
}
