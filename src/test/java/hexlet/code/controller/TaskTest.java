package hexlet.code.controller;

import hexlet.code.dto.AuthDto;
import hexlet.code.dto.CreateStatusDto;
import hexlet.code.dto.CreateTaskDto;
import hexlet.code.dto.CreateUserDto;
import hexlet.code.dto.GetStatusDto;
import hexlet.code.dto.GetTaskDto;
import hexlet.code.dto.GetUserDto;
import hexlet.code.dto.UpdateTaskDto;
import hexlet.code.repository.TaskRepository;
import hexlet.code.security.JwtTokenFilter;
import hexlet.code.utils.TestUtils;
import hexlet.code.utils.random.Random;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskTest {

    private final TaskRepository taskRepository;
    private final TestUtils testUtils;
    private final Random random;
    private String token;
    private GetUserDto getUserDto;
    private GetStatusDto getStatusDto;

    @Value("${base-api-url}")
    private String baseApiPath;

    @Autowired
    public TaskTest(TaskRepository taskRepository,
                    TestUtils testUtils,
                    Random random) {
        this.taskRepository = taskRepository;
        this.testUtils = testUtils;
        this.random = random;
    }

    @BeforeEach
    void registerAndAuth() {
        CreateUserDto createUserDto = random.randomCreateUserData();
        getUserDto = testUtils.defaultRegisterUser(createUserDto);
        token = testUtils.performAuthenticate(new AuthDto(createUserDto.getEmail(), createUserDto.getPassword()));
    }

    @BeforeEach
    void addStatus() {
        getStatusDto = testUtils.defaultAddStatus(new CreateStatusDto("READY_TO_DEPLOY"), token);
    }

    @AfterEach
    void clearDb() {
        testUtils.clearAllRepository();
    }

    @Test
    void successFindAllTest() throws UnsupportedEncodingException {
        testUtils.defaultAddTask(new CreateTaskDto("task", "description",
                null,
                getStatusDto.getId()
        ), token);
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .get(baseApiPath + TaskController.TASK_PATH)
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        List<GetTaskDto> responseBody = Arrays.asList(
                testUtils.fromJson(response.getContentAsString(),
                        GetTaskDto[].class));
        Assertions.assertThat(responseBody).hasSize(1);
    }

    @Test
    void successFindByIdTest() throws UnsupportedEncodingException {
        String name = "task";
        String description = "description";
        GetTaskDto createResponseBody = testUtils.defaultAddTask(new CreateTaskDto(name, description,
                null,
                getStatusDto.getId()
        ), token);
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .get(baseApiPath + TaskController.TASK_PATH + "/" + createResponseBody.getId())
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        GetTaskDto getByIdResponseBody = testUtils.fromJson(response.getContentAsString(),
                GetTaskDto.class);
        Assertions.assertThat(getByIdResponseBody.getName()).isEqualTo(name);
        Assertions.assertThat(getByIdResponseBody.getDescription()).isEqualTo(description);
    }

    @Test
    void createValidationErrorTest() {
        CreateTaskDto createTaskDto = new CreateTaskDto(null, "description",
                null,
                null);
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .post(baseApiPath + TaskController.TASK_PATH)
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.toJson(createTaskDto)));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    void createTaskByNameExistErrorTest() {
        String name = "task";
        String description = "description";
        CreateTaskDto createTaskDto = new CreateTaskDto(name, description,
                null,
                getStatusDto.getId()
        );
        testUtils.defaultAddTask(createTaskDto, token);
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .post(baseApiPath + TaskController.TASK_PATH)
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.toJson(createTaskDto)));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void createTaskExecutorNotExistErrorTest() {
        String name = "task";
        String description = "description";
        CreateTaskDto createTaskDto = new CreateTaskDto(name, description,
                getUserDto.getId() + 1,
                getStatusDto.getId()
        );
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .post(baseApiPath + TaskController.TASK_PATH)
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.toJson(createTaskDto)));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void createTaskStatusNotExistErrorTest() {
        String name = "task";
        String description = "description";
        CreateTaskDto createTaskDto = new CreateTaskDto(name, description,
                getUserDto.getId(),
                getStatusDto.getId() + 1
        );
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .post(baseApiPath + TaskController.TASK_PATH)
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.toJson(createTaskDto)));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void successCreateTest() {
        String name = "task";
        String description = "description";
        CreateTaskDto createTaskDto = new CreateTaskDto(name, description,
                getUserDto.getId(),
                getStatusDto.getId()
        );
        GetTaskDto responseBody = testUtils.defaultAddTask(createTaskDto, token);
        Assertions.assertThat(taskRepository.findById(responseBody.getId())).isPresent();
    }

    @Test
    void updateTaskStatusNotExistErrorTest() {
        String name = "task";
        String description = "description";
        CreateTaskDto createTaskDto = new CreateTaskDto(name, description,
                getUserDto.getId(),
                getStatusDto.getId()
        );
        GetTaskDto createResponseBody = testUtils.defaultAddTask(createTaskDto, token);
        UpdateTaskDto updateTaskDto = new UpdateTaskDto(
                name,
                description,
                getUserDto.getId(),
                getStatusDto.getId() + 1

        );
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .put(baseApiPath + TaskController.TASK_PATH + "/" + createResponseBody.getId())
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.toJson(updateTaskDto)));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void updateTaskExecutorNotExistErrorTest() {
        String name = "task";
        String description = "description";
        CreateTaskDto createTaskDto = new CreateTaskDto(name, description,
                getUserDto.getId(),
                getStatusDto.getId()
        );
        GetTaskDto createResponseBody = testUtils.defaultAddTask(createTaskDto, token);
        UpdateTaskDto updateTaskDto = new UpdateTaskDto(
                name,
                description,
                getUserDto.getId() + 1,
                getStatusDto.getId()

        );
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .put(baseApiPath + TaskController.TASK_PATH + "/" + createResponseBody.getId())
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.toJson(updateTaskDto)));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void successUpdateTaskTest() throws UnsupportedEncodingException {
        String name = "task";
        String description = "description";
        CreateTaskDto createTaskDto = new CreateTaskDto(name, description,
                getUserDto.getId(),
                getStatusDto.getId()
        );
        GetTaskDto createResponseBody = testUtils.defaultAddTask(createTaskDto, token);
        String newName = "new name";
        String newDescription = "new description";
        UpdateTaskDto updateTaskDto = new UpdateTaskDto(
                newName,
                newDescription,
                getUserDto.getId(),
                getStatusDto.getId()

        );
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .put(baseApiPath + TaskController.TASK_PATH + "/" + createResponseBody.getId())
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.toJson(updateTaskDto)));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        GetTaskDto updateResponseBody = testUtils.fromJson(response.getContentAsString(), GetTaskDto.class);
        Assertions.assertThat(updateResponseBody.getName()).isEqualTo(newName);
        Assertions.assertThat(updateResponseBody.getDescription()).isEqualTo(newDescription);
    }

    @Test
    void deleteForbiddenError() {
        CreateUserDto createUserDto = random.randomCreateUserData();
        testUtils.defaultRegisterUser(createUserDto);
        String otherToken = testUtils.performAuthenticate(new AuthDto(
                createUserDto.getEmail(),
                createUserDto.getPassword())
        );
        String name = "task";
        String description = "description";
        CreateTaskDto createTaskDto = new CreateTaskDto(name, description,
                getUserDto.getId(),
                getStatusDto.getId()
        );
        GetTaskDto createResponseBody = testUtils.defaultAddTask(createTaskDto, token);
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .delete(baseApiPath + TaskController.TASK_PATH + "/" + createResponseBody.getId())
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + otherToken));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void successDeleteTest() {
        String name = "task";
        String description = "description";
        CreateTaskDto createTaskDto = new CreateTaskDto(name, description,
                getUserDto.getId(),
                getStatusDto.getId()
        );
        GetTaskDto createResponseBody = testUtils.defaultAddTask(createTaskDto, token);
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .delete(baseApiPath + TaskController.TASK_PATH + "/" + createResponseBody.getId())
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(taskRepository.findById(createResponseBody.getId())).isNotPresent();
    }
}
