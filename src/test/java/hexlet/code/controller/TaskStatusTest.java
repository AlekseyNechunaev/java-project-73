package hexlet.code.controller;

import hexlet.code.dto.CreateStatusDto;
import hexlet.code.dto.GetStatusDto;
import hexlet.code.dto.CreateUserDto;
import hexlet.code.dto.AuthDto;
import hexlet.code.entity.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.security.JwtTokenFilter;
import hexlet.code.utils.TestUtils;
import hexlet.code.utils.random.Random;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskStatusTest {

    private final TestUtils testUtils;
    private final TaskStatusRepository taskStatusRepository;
    private final Random random;
    @Value("${base-api-url}")
    private String baseApiPath;

    private String token;

    private static final String STATUS_NAME = "READY TO DEPLOY";


    @Autowired
    public TaskStatusTest(TestUtils testUtils,
                          TaskStatusRepository taskStatusRepository,
                          Random random) {
        this.testUtils = testUtils;
        this.taskStatusRepository = taskStatusRepository;
        this.random = random;
    }

    @BeforeEach
    void registerAndAuth() throws UnsupportedEncodingException {
        CreateUserDto createUserDto = random.randomCreateUserData();
        testUtils.defaultRegisterUser(createUserDto);
        token = testUtils.performAuthenticate(new AuthDto(createUserDto.getEmail(), createUserDto.getPassword()));
    }

    @AfterEach
    void clearDb() {
        testUtils.clearAllRepository();
    }

    @Test
    void successFindAllTest() throws UnsupportedEncodingException {
        testUtils.defaultAddStatus(new CreateStatusDto(STATUS_NAME), token);
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .get(baseApiPath + TaskStatusController.TASK_STATUS_PATH));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        List<GetStatusDto> statuses = Arrays.asList(testUtils.fromJson(response.getContentAsString(),
                GetStatusDto[].class));
        Assertions.assertThat(statuses).hasSize(1);
        List<String> exceptedStatusesNames = Collections.singletonList(STATUS_NAME);
        List<String> statusesNames = statuses.stream()
                .map(GetStatusDto::getName)
                .toList();
        Assertions.assertThat(statusesNames).containsAll(exceptedStatusesNames);
    }

    @Test
    void successFindByIdTest() throws UnsupportedEncodingException {
        GetStatusDto addResponse = testUtils.defaultAddStatus(new CreateStatusDto(STATUS_NAME), token);
        MockHttpServletResponse response = testUtils.perform(
                MockMvcRequestBuilders
                        .get(baseApiPath + TaskStatusController.TASK_STATUS_PATH + "/" + addResponse.getId())
                        .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token)
        );
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        GetStatusDto findByIdResponse = testUtils.fromJson(response.getContentAsString(), GetStatusDto.class);
        Assertions.assertThat(findByIdResponse.getId()).isEqualTo(addResponse.getId());
        Assertions.assertThat(findByIdResponse.getName()).isEqualTo(STATUS_NAME);
    }

    @Test
    void errorUnauthorizedFindByIdTest() {
        GetStatusDto addResponse = testUtils.defaultAddStatus(new CreateStatusDto(STATUS_NAME), token);
        MockHttpServletResponse response = testUtils.perform(
                MockMvcRequestBuilders
                        .get(baseApiPath + TaskStatusController.TASK_STATUS_PATH + "/" + addResponse.getId())
        );
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void errorNotFoundFindByIdTest() {
        long notFoundId = 1;
        MockHttpServletResponse response = testUtils.perform(
                MockMvcRequestBuilders
                        .get(baseApiPath + TaskStatusController.TASK_STATUS_PATH + "/" + notFoundId)
                        .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token)
        );
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void errorCreateStatusExistByNameTest() {
        CreateStatusDto createStatusDto = new CreateStatusDto(STATUS_NAME);
        testUtils.defaultAddStatus(createStatusDto, token);
        MockHttpServletResponse response = testUtils.perform(
                MockMvcRequestBuilders
                        .post(baseApiPath + TaskStatusController.TASK_STATUS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUtils.toJson(createStatusDto))
                        .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token)
        );
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void errorUnauthorizedCreateTest() {
        CreateStatusDto createStatusDto = new CreateStatusDto(STATUS_NAME);
        MockHttpServletResponse response = testUtils.perform(
                MockMvcRequestBuilders
                        .post(baseApiPath + TaskStatusController.TASK_STATUS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUtils.toJson(createStatusDto))
        );
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void successCreateTest() {
        CreateStatusDto createStatusDto = new CreateStatusDto(STATUS_NAME);
        GetStatusDto response = testUtils.defaultAddStatus(createStatusDto, token);
        Optional<TaskStatus> statusFromDb = taskStatusRepository.findById(response.getId());
        Assertions.assertThat(statusFromDb).isPresent();
    }

    @Test
    void errorUnauthorizedUpdateTest() {
        CreateStatusDto createStatusDto = new CreateStatusDto(STATUS_NAME);
        long id = 1;
        MockHttpServletResponse response = testUtils.perform(
                MockMvcRequestBuilders
                        .put(baseApiPath + TaskStatusController.TASK_STATUS_PATH + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUtils.toJson(createStatusDto))
        );
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void errorNotFoundByIdUpdateTest() {
        CreateStatusDto createStatusDto = new CreateStatusDto(STATUS_NAME);
        long notFoundId = 1;
        MockHttpServletResponse response = testUtils.perform(
                MockMvcRequestBuilders
                        .put(baseApiPath + TaskStatusController.TASK_STATUS_PATH + "/" + notFoundId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUtils.toJson(createStatusDto))
                        .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token)
        );
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void errorStatusExistByNameUpdateTest() {
        CreateStatusDto createFirstStatusRequest = new CreateStatusDto(STATUS_NAME);
        GetStatusDto createFirstStatusResponse = testUtils.defaultAddStatus(createFirstStatusRequest, token);
        String otherStatus = "READY TO TEST";
        CreateStatusDto createSecondStatusRequest = new CreateStatusDto(otherStatus);
        testUtils.defaultAddStatus(createSecondStatusRequest, token);
        MockHttpServletResponse response = testUtils.perform(
                MockMvcRequestBuilders
                        .put(baseApiPath + TaskStatusController.TASK_STATUS_PATH + "/"
                                + createFirstStatusResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUtils.toJson(createSecondStatusRequest))
                        .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token)
        );
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void successUpdateTest() {
        CreateStatusDto createStatusDto = new CreateStatusDto(STATUS_NAME);
        GetStatusDto createResponse = testUtils.defaultAddStatus(createStatusDto, token);
        String otherStatus = "READY TO TEST";
        CreateStatusDto updateRequest = new CreateStatusDto(otherStatus);
        MockHttpServletResponse response = testUtils.perform(
                MockMvcRequestBuilders
                        .put(baseApiPath + TaskStatusController.TASK_STATUS_PATH + "/"
                                + createResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUtils.toJson(updateRequest))
                        .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token)
        );
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void errorNotFoundByIdDeleteTest() {
        long notFoundId = 1;
        MockHttpServletResponse response = testUtils.perform(
                MockMvcRequestBuilders
                        .delete(baseApiPath + TaskStatusController.TASK_STATUS_PATH + "/" + notFoundId)
                        .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token)
        );
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void errorUnauthorizedDeleteTest() {
        long id = 1;
        MockHttpServletResponse response = testUtils.perform(
                MockMvcRequestBuilders
                        .delete(baseApiPath + TaskStatusController.TASK_STATUS_PATH + "/" + id)
        );
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void successDeleteTest() {
        CreateStatusDto createStatusDto = new CreateStatusDto(STATUS_NAME);
        GetStatusDto createResponse = testUtils.defaultAddStatus(createStatusDto, token);
        MockHttpServletResponse response = testUtils.perform(
                MockMvcRequestBuilders
                        .delete(baseApiPath + TaskStatusController.TASK_STATUS_PATH + "/" + createResponse.getId())
                        .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token)
        );
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
