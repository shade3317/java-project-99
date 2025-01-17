package hexlet.code;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc        mockMvc;
    @Autowired
    private ObjectMapper   objectMapper;
    @Autowired
    private UserMapper     userMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelGenerator modelsGenerator;
    private User           testUser;
    private User           anotherUser;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor tokenAnotherUser;


    @BeforeEach
    public void setUser() {
        testUser = Instancio.of(modelsGenerator.getTestUser()).create();
        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));

        anotherUser      = Instancio.of(modelsGenerator.getTestUser()).create();
        tokenAnotherUser = jwt().jwt(builder -> builder.subject(anotherUser.getEmail()));
    }

    @Test
    public void testIndex() throws Exception {
        userRepository.save(testUser);
        var result = mockMvc.perform(get("/api/users").with(token))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        userRepository.save(testUser);
        var request = get("/api/users/" + testUser.getId()).with(token);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                v -> v.node("lastName").isEqualTo(testUser.getLastName()),
                v -> v.node("email").isEqualTo(testUser.getEmail()));
    }

    @Test
    public void testCreate() throws Exception {
        var createDto = userMapper.mapToCreateDto(testUser);

        var request = post("/api/users").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());
        var user = userRepository.findByEmail(testUser.getEmail()).orElse(null);
        assertNotNull(user);
        assertThat(user.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    public void testUpdate() throws Exception {
        userRepository.save(testUser);
        var updateDto = new UserUpdateDTO();
        updateDto.setFirstName(JsonNullable.of("name1"));
        updateDto.setLastName(JsonNullable.of("lastName1"));

        var request = put("/api/users/" + testUser.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var user = userRepository.findById(testUser.getId()).get();
        assertNotNull(user);
        assertThat(user.getFirstName()).isEqualTo(updateDto.getFirstName().get());
        assertThat(user.getLastName()).isEqualTo(updateDto.getLastName().get());
    }

    @Test
    public void testDelete() throws Exception {
        userRepository.save(testUser);
        var request = delete("/api/users/" + testUser.getId()).with(token);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(userRepository.existsById(testUser.getId())).isEqualTo(false);
    }

    @Test
    public void testUpdateUserAnotherUser() throws Exception {
        userRepository.save(testUser);
        userRepository.save(anotherUser);

        var updateDto = new UserUpdateDTO();
        updateDto.setFirstName(JsonNullable.of("name2"));
        updateDto.setLastName(JsonNullable.of("lastName2"));

        var request = put("/api/users/" + testUser.getId())
                .with(tokenAnotherUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto));

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteUserAnotherUser() throws Exception {
        userRepository.save(testUser);
        userRepository.save(anotherUser);

        var request = delete("/api/users/" + testUser.getId()).with(tokenAnotherUser);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }
}
