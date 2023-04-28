package hexlet.code.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile(TestConfig.TEST_PROFILE)
@ComponentScan(basePackages = "hexlet.code")
@PropertySource(value = "classpath:application.yaml")
public class TestConfig {
    public static final String TEST_PROFILE = "test";
}
