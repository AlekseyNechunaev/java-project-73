package hexlet.code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.controller.AuthController;
import hexlet.code.controller.TaskStatusController;
import hexlet.code.controller.UserController;
import hexlet.code.dto.CreateStatusDto;
import hexlet.code.dto.GetStatusDto;
import hexlet.code.dto.GetUserDto;
import hexlet.code.dto.CreateUserDto;
import hexlet.code.dto.AuthDto;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.JwtTokenFilter;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;

@Component
public class TestUtils {

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;

    @Value("${base-api-url}")
    private String baseApiPath;

    @Autowired
    public TestUtils(ObjectMapper objectMapper,
                     MockMvc mockMvc,
                     UserRepository userRepository,
                     TaskStatusRepository taskStatusRepository) {
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
        this.taskStatusRepository = taskStatusRepository;
    }

    public MockHttpServletResponse perform(MockHttpServletRequestBuilder requestBuilder) {
        try {
            return mockMvc.perform(requestBuilder)
                    .andReturn()
                    .getResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GetUserDto defaultRegisterUser(CreateUserDto userDto) {
        MockHttpServletResponse response = perform(MockMvcRequestBuilders
                .post(baseApiPath + UserController.USER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(userDto)));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        try {
            return fromJson(response.getContentAsString(), GetUserDto.class);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public GetStatusDto defaultAddStatus(CreateStatusDto dto, String token) {
        MockHttpServletResponse response = perform(MockMvcRequestBuilders
                .post(baseApiPath + TaskStatusController.TASK_STATUS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token)
                .content(toJson(dto)));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        try {
            return fromJson(response.getContentAsString(), GetStatusDto.class);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String performAuthenticate(AuthDto authDto) throws UnsupportedEncodingException {
        MockHttpServletResponse response = perform(MockMvcRequestBuilders
                .post(baseApiPath + AuthController.AUTH_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(authDto)));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        String token = response.getContentAsString();
        Assertions.assertThat(token).isNotEmpty();
        return token;
    }

    public <T> T fromJson(String json, Class<T> mappedClass) {
        try {
            return objectMapper.readValue(json, mappedClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> String toJson(T requestBody) {
        try {
            return objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearAllRepository() {
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
    }
}
