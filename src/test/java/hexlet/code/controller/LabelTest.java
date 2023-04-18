package hexlet.code.controller;

import hexlet.code.dto.AuthDto;
import hexlet.code.dto.CreateLabelDto;
import hexlet.code.dto.CreateUserDto;
import hexlet.code.dto.GetLabelDto;
import hexlet.code.repository.LabelRepository;
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
public class LabelTest {

    private final TestUtils testUtils;
    private final LabelRepository labelRepository;
    private final Random random;
    @Value("${base-api-url}")
    private String baseApiPath;

    private String token;

    private static final String DEFAULT_LABEL_NAME = "LABEL";
    private static final String OTHER_NAME_LABEL = "OTHER LABEL";

    @Autowired
    public LabelTest(TestUtils testUtils, LabelRepository labelRepository, Random random) {
        this.testUtils = testUtils;
        this.labelRepository = labelRepository;
        this.random = random;
    }

    @BeforeEach
    void registerAndAuth() {
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
        CreateLabelDto createRequestBody = new CreateLabelDto(DEFAULT_LABEL_NAME);
        testUtils.defaultAddLabel(createRequestBody, token);
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .get(baseApiPath + LabelController.LABEL_PATH)
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        List<GetLabelDto> responseBody = Arrays.asList(
                testUtils.fromJson(response.getContentAsString(),
                        GetLabelDto[].class));
        Assertions.assertThat(responseBody).hasSize(1);
    }

    @Test
    void successFindByIdTest() throws UnsupportedEncodingException {
        CreateLabelDto createRequestBody = new CreateLabelDto(DEFAULT_LABEL_NAME);
        GetLabelDto createResponseBody = testUtils.defaultAddLabel(createRequestBody, token);
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .get(baseApiPath + LabelController.LABEL_PATH + "/" + createResponseBody.getId())
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        GetLabelDto responseBody = testUtils.fromJson(response.getContentAsString(), GetLabelDto.class);
        Assertions.assertThat(responseBody.getName()).isEqualTo(createRequestBody.getName());
    }

    @Test
    void successCreateTest() {
        CreateLabelDto createRequestBody = new CreateLabelDto(DEFAULT_LABEL_NAME);
        GetLabelDto createResponseBody = testUtils.defaultAddLabel(createRequestBody, token);
        Assertions.assertThat(labelRepository.findById(createResponseBody.getId())).isPresent();
    }

    @Test
    void createValidationErrorTest() {
        CreateLabelDto requestBody = new CreateLabelDto();
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .post(baseApiPath + LabelController.LABEL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.toJson(requestBody))
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    void createErrorExistByNameLabelTest() {
        CreateLabelDto createRequestBody = new CreateLabelDto(DEFAULT_LABEL_NAME);
        testUtils.defaultAddLabel(createRequestBody, token);
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .post(baseApiPath + LabelController.LABEL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.toJson(createRequestBody))
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void successUpdateTest() throws UnsupportedEncodingException {
        CreateLabelDto createRequestBody = new CreateLabelDto(DEFAULT_LABEL_NAME);
        GetLabelDto createResponseBody = testUtils.defaultAddLabel(createRequestBody, token);
        CreateLabelDto updateRequestBody = new CreateLabelDto(OTHER_NAME_LABEL);
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .put(baseApiPath + LabelController.LABEL_PATH + "/" + createResponseBody.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.toJson(updateRequestBody))
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        GetLabelDto updateResponseBody = testUtils.fromJson(response.getContentAsString(), GetLabelDto.class);
        Assertions.assertThat(updateResponseBody.getName()).isEqualTo(updateRequestBody.getName());
    }

    @Test
    void updateErrorExistByNameLabelTest() {
        CreateLabelDto createRequestBodyFirstLabel = new CreateLabelDto(DEFAULT_LABEL_NAME);
        testUtils.defaultAddLabel(createRequestBodyFirstLabel, token);
        CreateLabelDto createRequestBodySecondLabel = new CreateLabelDto(OTHER_NAME_LABEL);
        GetLabelDto createResponseBodySecondLabel = testUtils.defaultAddLabel(createRequestBodySecondLabel, token);
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .put(baseApiPath + LabelController.LABEL_PATH + "/" + createResponseBodySecondLabel.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.toJson(createRequestBodyFirstLabel))
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void successDeleteTest() {
        CreateLabelDto createRequestBody = new CreateLabelDto(DEFAULT_LABEL_NAME);
        GetLabelDto createResponseBody = testUtils.defaultAddLabel(createRequestBody, token);
        MockHttpServletResponse response = testUtils.perform(MockMvcRequestBuilders
                .delete(baseApiPath + LabelController.LABEL_PATH + "/" + createResponseBody.getId())
                .header(HttpHeaders.AUTHORIZATION, JwtTokenFilter.BEARER_PREFIX + " " + token));
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}
