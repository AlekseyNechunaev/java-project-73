package hexlet.code.utils.random;

import com.github.javafaker.Faker;
import hexlet.code.dto.CreateUserDto;
import org.springframework.stereotype.Component;

@Component
public class Random {

    private final Faker faker;

    public Random(Faker faker) {
        this.faker = faker;
    }

    public CreateUserDto randomCreateUserData() {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setFirstName(faker.name().firstName());
        createUserDto.setLastName(faker.name().lastName());
        createUserDto.setEmail(faker.regexify("[a-z]{10}") + "@gmail.com");
        createUserDto.setPassword(faker.regexify("[a-zA-Z0-9]{10}"));
        return createUserDto;
    }
}
