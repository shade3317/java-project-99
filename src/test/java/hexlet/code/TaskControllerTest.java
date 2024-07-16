package hexlet.code;
import hexlet.code.dto.task.TaskUpdateDto;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;

import java.util.HashMap;
import java.util.Set;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private MockMvc              mockMvc;
    @Autowired
    private UserRepository       userRepository;
    @Autowired
    private TaskRepository       taskRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TaskMapper           taskMapper;
    @Autowired
    private ObjectMapper         objectMapper;
    @Autowired
    private LabelRepository      labelRepository;
    @Autowired
    private ModelGenerator       modelGenerator;
    private User                 testUser;
    private Task                 testTask;
    private TaskStatus           testStatus;
    private Label                testLabel;

    private JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(modelGenerator.getTestUser()).create();
        userRepository.save(testUser);

        testStatus = Instancio.of(modelGenerator.getTestStatus()).create();
        taskStatusRepository.save(testStatus);

        testLabel = Instancio.of(modelGenerator.getTestLabel()).create();
        labelRepository.save(testLabel);

        testTask = Instancio.of(modelGenerator.getTestTask()).create();
        testTask.setTaskStatus(testStatus);
        testTask.setAssignee(testUser);
        testTask.setLabels(Set.of(testLabel));
        taskRepository.save(testTask);

        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/tasks").with(token))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/tasks/" + testTask.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("index").isEqualTo(testTask.getIndex()),
                v -> v.node("assignee_id").isEqualTo(testTask.getAssignee().getId()),
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("content").isEqualTo(testTask.getDescription()),
                v -> v.node("status").isEqualTo(testTask.getTaskStatus().getSlug()));
    }

    @Test
    public void testCreate() throws Exception {
        var data = new HashMap<>();
        data.put("title", testTask.getName());
        data.put("index", testTask.getIndex());
        data.put("content", testTask.getDescription());
        data.put("status", testTask.getTaskStatus().getSlug());
        data.put("assignee_id", testTask.getAssignee().getId());

        var request = post("/api/tasks").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = taskRepository.findById(testTask.getId()).orElse(null);
        assertNotNull(task);
        assertThat(task.getIndex()).isEqualTo(testTask.getIndex());
        assertThat(task.getAssignee().getId()).isEqualTo(testTask.getAssignee().getId());
        assertThat(task.getDescription()).isEqualTo(testTask.getDescription());
        assertThat(task.getTaskStatus().getSlug()).isEqualTo(testTask.getTaskStatus().getSlug());
        assertThat(task.getName()).isEqualTo(testTask.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new TaskUpdateDto();
        data.setTitle(JsonNullable.of("title1"));
        data.setDescription(JsonNullable.of("description1"));

        var request = put("/api/tasks/" + testTask.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        testTask = taskRepository.findById(testTask.getId()).orElse(null);
        assertNotNull(testTask);
        assertThat(testTask.getDescription()).isEqualTo(data.getDescription().get());
        assertThat(testTask.getName()).isEqualTo(data.getTitle().get());
    }

    @Test
    public void testDestroy() throws Exception {
        var request = delete("/api/tasks/" + testTask.getId()).with(token);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(taskRepository.existsById(testTask.getId())).isEqualTo(false);
    }
}
