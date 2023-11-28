package controllers;
import application.BalootApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.InvalidCreditRange;
import exceptions.NotExistentUser;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import service.Baloot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = BalootApplication.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @MockBean
    private Baloot balootMock;

    @Mock
    private User user;

    @BeforeEach
    public void setUp() {
        userController.setBaloot(balootMock);
    }

    @Test
    public void testGetUserByExistedUserId() throws Exception {
        String userId = "1";
        User user = new User("username", "password", "mailto:user@gmail.com", "2000-01-01", "address");
        when(balootMock.getUserById(userId)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.password").value(user.getPassword()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.birthDate").value(user.getBirthDate()))
                .andExpect(jsonPath("$.address").value(user.getAddress()));
    }

    @Test
    public void testGetUserByNonExistedUserId() throws Exception {
        when(balootMock.getUserById("NonExistedUserId"))
                .thenThrow(new NotExistentUser());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/NonExistedUserId"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddCreditByExistedUserValidCreditCorrectFormat() throws Exception {
        String userId = "1";
        User user = new User("username", "password", "mailto:user@gmail.com", "2000-01-01", "address");
        when(balootMock.getUserById(userId)).thenReturn(user);

        Map<String, String> testContent = new HashMap<>();
        testContent.put("credit", "100.0");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/"+userId+"/credit")
                        .content(new ObjectMapper().writeValueAsString(testContent))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().string("credit added successfully!"));
    }

    @Test
    public void testAddCreditByNonExistentUserValidCreditCorrectFormat() throws Exception {
        when(balootMock.getUserById("NonExistedUserId"))
                .thenThrow(new NotExistentUser());
        Map<String, String> testContent = new HashMap<>();
        testContent.put("credit", "100.0");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/NonExistedUserId/credit")
                        .content(new ObjectMapper().writeValueAsString(testContent))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testAddCreditInvalidNumberFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/testUserId/credit")
                        .content(new ObjectMapper().writeValueAsString(Collections.singletonMap("credit", "invalidValue")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testAddCreditInvalidNumberFormat2() throws Exception {
        when(balootMock.getUserById("1212"))
                .thenThrow(new NumberFormatException());

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1212/credit")
                        .content(new ObjectMapper().writeValueAsString(Collections.singletonMap("credit", "invalidValue")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testAddCreditInvalidNumberFormat3() throws Exception {
        Map<String, String> testContent = new HashMap<>();
        testContent.put("credit", "1500.0aaaa");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1212/credit")
                        .content(new ObjectMapper().writeValueAsString(Collections.singletonMap("credit", "invalidValue")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testAddCreditInvalidNumberFormat4() throws Exception {
        String userId = "1";
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("mailto:user@gmail.com");
        user.setBirthDate("2002-5-10");
        user.setAddress("No where!");
        when(balootMock.getUserById(userId)).thenReturn(user);

        float cre = Float.parseFloat("1500.0");
        doThrow(new NumberFormatException()).when(user).addCredit(cre);

        Map<String, String> testContent = new HashMap<>();
        testContent.put("credit", "1500.0");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/"+ userId +"/credit")
                        .content(new ObjectMapper().writeValueAsString(testContent))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testAddCreditByExistUserInvalidCreditCorrectFormat() throws Exception {
        String userId = "1";
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("mailto:user@gmail.com");
        user.setBirthDate("2002-5-10");
        user.setAddress("No where!");
        when(balootMock.getUserById(userId)).thenReturn(user);

        float cre = Float.parseFloat("1500.0");
        doThrow(new InvalidCreditRange()).when(user).addCredit(cre);

        Map<String, String> testContent = new HashMap<>();
        testContent.put("credit", "1500.0");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/"+ userId +"/credit")
                        .content(new ObjectMapper().writeValueAsString(testContent))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
}
