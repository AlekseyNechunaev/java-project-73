package hexlet.code.controller;

import hexlet.code.dto.CreateUserDto;
import hexlet.code.dto.GetUserDto;
import hexlet.code.dto.ValidationErrorDto;
import hexlet.code.entity.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import hexlet.code.utils.random.Random;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UsersTest {

    private final TestUtils testUtils;
    private final UserRepository userRepository;
    private final Random random;

    @Value("${base-api-url}")
    private String baseApiPath;

    @Autowired
    public UsersTest(TestUtils testUtils, UserRepository userRepository, Random random) {
        this.testUtils = testUtils;
        this.userRepository = userRepository;
        this.random = random;
    }

    @BeforeEach
    void clearDb() {
        userRepository.deleteAll();
    }

    @Test
    void registerUserWithNotValidDataTest() throws Exception {
        CreateUserDto notValidUserDto = new CreateUserDto();
        notValidUserDto.setEmail("asdasda.gmail.ru");
        notValidUserDto.setPassword("qw");
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .post(baseApiPath + UserController.USER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.toJson(notValidUserDto)));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        Assertions.assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        List<ValidationErrorDto> errors = Arrays.asList(
                testUtils.fromJson(response.getContentAsString(), ValidationErrorDto[].class));
        List<String> exceptedErrorFields = List.of("lastName", "password", "email", "firstName");
        List<String> errorFields = errors.stream()
                .map(ValidationErrorDto::getFieldName)
                .toList();
        Assertions.assertThat(errorFields.size()).isEqualTo(exceptedErrorFields.size());
        Assertions.assertThat(errorFields).containsAll(exceptedErrorFields);
    }

    @Test
    void successRegisterUserTest() {
        CreateUserDto createUserDto = random.randomCreateUserData();
        GetUserDto response = testUtils.defaultRegisterUser(createUserDto);
        Assertions.assertThat(createUserDto.getEmail()).isEqualTo(response.getEmail());
        Assertions.assertThat(createUserDto.getFirstName()).isEqualTo(response.getFirstName());
        Assertions.assertThat(createUserDto.getLastName()).isEqualTo(response.getLastName());
        User userFromDb = userRepository.findById(response.getId()).get();
        Assertions.assertThat(createUserDto.getEmail()).isEqualTo(userFromDb.getEmail());
        Assertions.assertThat(createUserDto.getFirstName()).isEqualTo(userFromDb.getFirstName());
        Assertions.assertThat(createUserDto.getLastName()).isEqualTo(userFromDb.getLastName());
    }

    @Test
    void registerUserWithErrorExistUserByEmailTest() {
        CreateUserDto successCreateRequest = random.randomCreateUserData();
        GetUserDto successCreateResponse = testUtils.defaultRegisterUser(successCreateRequest);
        Assertions.assertThat(userRepository.findById(successCreateResponse.getId())).isPresent();
        CreateUserDto failedCreateUserRequest = random.randomCreateUserData();
        failedCreateUserRequest.setEmail(successCreateRequest.getEmail());
        MockHttpServletResponse failedResponse = testUtils.perform(MockMvcRequestBuilders
                .post(baseApiPath + UserController.USER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.toJson(failedCreateUserRequest)));
        Assertions.assertThat(failedResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void userGetByIdNotFoundTest() {
        long notFoundId = 1L;
        MockHttpServletResponse notFoundResponse = testUtils.perform(MockMvcRequestBuilders
                .get(baseApiPath + UserController.USER_PATH + notFoundId));
        Assertions.assertThat(notFoundResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void userSuccessGetByIdTest() throws UnsupportedEncodingException {
        CreateUserDto registerRequest = random.randomCreateUserData();
        GetUserDto registerResponse = testUtils.defaultRegisterUser(registerRequest);
        Assertions.assertThat(userRepository.findById(registerResponse.getId())).isPresent();
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .get(baseApiPath + UserController.USER_PATH + "/" + registerResponse.getId()));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        GetUserDto getByIdResponse = testUtils.fromJson(response.getContentAsString(), GetUserDto.class);
        Assertions.assertThat(getByIdResponse.getEmail()).isEqualTo(registerResponse.getEmail());
        Assertions.assertThat(getByIdResponse.getFirstName()).isEqualTo(registerResponse.getFirstName());
        Assertions.assertThat(getByIdResponse.getLastName()).isEqualTo(registerResponse.getLastName());
    }

    @Test
    void successFindAllTest() throws UnsupportedEncodingException {
        CreateUserDto firstRegisterRequest = random.randomCreateUserData();
        GetUserDto firstRegisterResponse = testUtils.defaultRegisterUser(firstRegisterRequest);
        CreateUserDto secondRegisterRequest = random.randomCreateUserData();
        GetUserDto secondRegisterResponse = testUtils.defaultRegisterUser(secondRegisterRequest);
        Assertions.assertThat(userRepository.findById(firstRegisterResponse.getId())).isPresent();
        Assertions.assertThat(userRepository.findById(secondRegisterResponse.getId())).isPresent();
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .get(baseApiPath + UserController.USER_PATH));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        List<String> exceptedMails = List.of(firstRegisterRequest.getEmail(), secondRegisterRequest.getEmail());
        List<String> userEmailsFromResponseAll = Arrays.stream(
                testUtils.fromJson(response.getContentAsString(), GetUserDto[].class)
        )
                .map(GetUserDto::getEmail)
                .toList();
        Assertions.assertThat(exceptedMails.size()).isEqualTo(userEmailsFromResponseAll.size());
        Assertions.assertThat(userEmailsFromResponseAll).containsAll(exceptedMails);
    }

    @Test
    void updateUserWithErrorExistUserByEmailTest() {
        CreateUserDto firstRegisterRequest = random.randomCreateUserData();
        GetUserDto firstRegisterResponse = testUtils.defaultRegisterUser(firstRegisterRequest);
        CreateUserDto secondRegisterRequest = random.randomCreateUserData();
        GetUserDto secondRegisterResponse = testUtils.defaultRegisterUser(secondRegisterRequest);
        Assertions.assertThat(userRepository.findById(firstRegisterResponse.getId())).isPresent();
        Assertions.assertThat(userRepository.findById(secondRegisterResponse.getId())).isPresent();
        CreateUserDto failedUpdateRequest = random.randomCreateUserData();
        failedUpdateRequest.setEmail(secondRegisterResponse.getEmail());
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .put(baseApiPath + UserController.USER_PATH + "/" + firstRegisterResponse.getId()));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void updateUserNotFoundByIdTest() {
        CreateUserDto updateRequest = random.randomCreateUserData();
        long notFoundId = 1L;
        MockHttpServletResponse notFoundResponse = testUtils.perform(MockMvcRequestBuilders
                .put(baseApiPath + UserController.USER_PATH + notFoundId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.toJson(updateRequest)));
        Assertions.assertThat(notFoundResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void successUpdateUserTest() throws UnsupportedEncodingException {
        CreateUserDto registerRequest = random.randomCreateUserData();
        GetUserDto registerResponse = testUtils.defaultRegisterUser(registerRequest);
        Assertions.assertThat(userRepository.findById(registerResponse.getId())).isPresent();
        CreateUserDto updateRequest = random.randomCreateUserData();
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .put(baseApiPath + UserController.USER_PATH + "/" + registerResponse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.toJson(updateRequest)));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        GetUserDto successUpdateResponse = testUtils.fromJson(response.getContentAsString(), GetUserDto.class);
        Assertions.assertThat(successUpdateResponse.getLastName()).isEqualTo(updateRequest.getLastName());
        Assertions.assertThat(successUpdateResponse.getFirstName()).isEqualTo(updateRequest.getFirstName());
        Assertions.assertThat(successUpdateResponse.getEmail()).isEqualTo(updateRequest.getEmail());
    }

    @Test
    void deleteUserNotFoundTest() {
        long notFoundId = 1L;
        MockHttpServletResponse notFoundResponse = testUtils.perform(MockMvcRequestBuilders
                .delete(baseApiPath + UserController.USER_PATH + "/" + notFoundId)
        );
        Assertions.assertThat(notFoundResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void successDeleteUserTest() {
        CreateUserDto registerRequest = random.randomCreateUserData();
        GetUserDto registerResponse = testUtils.defaultRegisterUser(registerRequest);
        Assertions.assertThat(userRepository.findById(registerResponse.getId())).isPresent();
        MockHttpServletResponse notFoundResponse = testUtils.perform(MockMvcRequestBuilders
                .delete(baseApiPath + UserController.USER_PATH + "/" + registerResponse.getId())
        );
        Assertions.assertThat(notFoundResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(userRepository.findById(registerResponse.getId())).isNotPresent();
    }
}
