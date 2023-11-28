package controllers;

import application.BalootApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.InvalidCreditRange;
import exceptions.NotExistentCommodity;
import exceptions.NotExistentUser;
import model.Comment;
import model.Commodity;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static defines.Errors.NOT_EXISTENT_COMMODITY;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = BalootApplication.class)
public class CommoditiesControllerAPITest {
    @Mock
    private Commodity commodity;
    @Mock private User user;
    @Mock private NotExistentCommodity notExistentCommodity;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CommoditiesController commoditiesController_apiTest;
    @MockBean
    private Baloot balootMock;

    @BeforeEach
    public void setUp() {
        commoditiesController_apiTest.setBaloot(balootMock);
    }

    // API TEST
    @Test
    public void getCommoditiesTest() {
        commodity.setId("5");
        commodity.setName("Majid");

        ArrayList<Commodity> returnValue = new ArrayList<>();
        returnValue.add(commodity);

        when(balootMock.getCommodities()).thenReturn(returnValue);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/commodities"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()", is(returnValue.size())))
                    .andExpect(jsonPath("$[0].id", is(returnValue.get(0).getId())))
                    .andExpect(jsonPath("$[0].name", is(returnValue.get(0).getName())));
        } catch(Exception e) {

        }
    }

    @Test
    public void getExistingCommodity() throws NotExistentCommodity {

        String commodityId = "1";
        Commodity commodity = new Commodity();
        commodity.setId(commodityId);
        commodity.setName("product1 ");
        when(balootMock.getCommodityById(commodityId)).thenReturn(commodity);
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/commodities/" + commodityId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(commodity.getId()))
                    .andExpect(jsonPath("$.name").value(commodity.getName()));
        } catch (Exception e) {

        }

    }
    @Test
    public void getInvalidCommodity() throws NotExistentCommodity {
        when(balootMock.getCommodityById(anyString())).thenThrow(notExistentCommodity);

        try {
            mockMvc.perform(get("/commodities/" + "1"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.id").doesNotExist());
        } catch(Exception e) {

        }
    }

    @Test
    public void rateExistingCommodityWithValidRate() throws NotExistentCommodity {
        String commodityId = "1111";
        Commodity commodity = new Commodity();
        commodity.setId(commodityId);
        commodity.setName("product1 ");
        Map<String, String> testContent = new HashMap<>();
        testContent.put("rate", "5");
        testContent.put("username","user1");

        when(balootMock.getCommodityById(commodityId)).thenReturn(commodity);

        try {
            mockMvc.perform(post("/commodities/" + commodityId + "/rate")
                            .content(new ObjectMapper().writeValueAsString(testContent))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("rate added successfully!"));
        } catch (Exception e) {

        }
    }

    @Test
    public void rateInvalidCommodityWithValidRate() throws NotExistentCommodity {
        String commodityId = "10";

        Map<String, String> input = new HashMap<>();
        input.put("username", "majid");
        input.put("rate", "10");

        when(balootMock.getCommodityById(commodityId)).thenThrow(new NotExistentCommodity());

        try {
            mockMvc.perform(post("/commodities/" + commodityId + "/rate")
                            .content(new ObjectMapper().writeValueAsString(input))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(NOT_EXISTENT_COMMODITY));
        } catch(Exception e) {

        }
    }

    @Test
    public void rateExistingCommodityWithInvalidRate() throws NumberFormatException, NotExistentCommodity {
        String commodityId = "111";

        Commodity commodity = new Commodity();
        commodity.setId(commodityId);
        commodity.setName("product1 ");

        Map<String, String> testContent = new HashMap<>();
        testContent.put("rate", "10aa");
        testContent.put("username","user1");

        when(balootMock.getCommodityById(commodityId)).thenReturn(commodity);

        try {
            mockMvc.perform(post("/commodities/" + commodityId + "/rate")
                            .content(new ObjectMapper().writeValueAsString(testContent))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(not(containsString("rate added successfully!"))));
        } catch (Exception e) {

        }
    }

    @Test
    public void addCommentForExistingCommodity() throws NotExistentUser {
        String username = "user";
        String userEmail = "user@test.com";

        user.setUsername(username);
        user.setEmail(userEmail);

        Map<String, String> input = new HashMap<>();
        input.put("username", username);
        input.put("comment", "This is a test comment!");

        when(balootMock.generateCommentId()).thenReturn(1);
        when(balootMock.getUserById(anyString())).thenReturn(user);
        when(user.getEmail()).thenReturn(userEmail);
        when(user.getUsername()).thenReturn(username);
        doNothing().when(balootMock).addComment(ArgumentMatchers.isA(Comment.class));

        try {
            mockMvc.perform(post("/commodities/" + "111" + "/comment")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("comment added successfully!"));
        } catch (Exception e) {

        }
    }

    @Test
    public void addCommentForInvalidCommodity() throws NotExistentUser {

        String commentText = "commenttext";
        String commodityId = "1";
        String userId = "1";
        User user = new User("username", "password", "mailto:user@gmail.com", "2000-01-01", "address");

        Map<String, String> testContent = new HashMap<>();
        testContent.put("username", user.getUsername());
        testContent.put("comment",commentText);

        when(balootMock.getUserById(userId)).thenReturn(user);


        try {
            mockMvc.perform(post("/commodities/" + commodityId + "/comment")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(testContent)))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {

        }
    }

    @Test
    public void getCommodityCommentTest() {
        Comment comment = new Comment();
        comment.setUsername("majid");
        comment.setUserEmail("majid@gmail.com");
        comment.setCommodityId(123);
        comment.setText("This is a test comment!");

        ArrayList<Comment> returnValue = new ArrayList<>();
        returnValue.add(comment);

        when(balootMock.getCommentsForCommodity(anyInt())).thenReturn(returnValue);

        try {
            mockMvc.perform(get("/commodities/" + "123" + "/comment"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()", is(returnValue.size())))
                    .andExpect(jsonPath("$[0].id", is(returnValue.get(0).getId())))
                    .andExpect(jsonPath("$[0].userEmail", is(returnValue.get(0).getUserEmail())))
                    .andExpect(jsonPath("$[0].username", is(returnValue.get(0).getUsername())))
                    .andExpect(jsonPath("$[0].commodityId", is(returnValue.get(0).getCommodityId())))
                    .andExpect(jsonPath("$[0].text", is(returnValue.get(0).getText())));
        } catch (Exception e) {

        }
    }

    @Test
    public void searchCommoditiesByName() {


        String searchBy = "name";
        String searchValue = "user5";
        ArrayList<Commodity> test_commodities = new ArrayList<>();

        Commodity commodity = new Commodity();
        commodity.setId("123");
        commodity.setName("product1 ");
        test_commodities.add(commodity);

        Map<String, String> testContent = new HashMap<>();
        testContent.put("searchOption", searchBy);
        testContent.put("searchValue",searchValue);


        when(balootMock.filterCommoditiesByName(searchValue)).thenReturn(test_commodities);

        try {
            mockMvc.perform(post("/commodities/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(testContent)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(test_commodities.size()))
                    .andExpect(jsonPath("$[0].id").value(test_commodities.get(0).getId()))
                    .andExpect(jsonPath("$[0].name").value(test_commodities.get(0).getName()));
        } catch (Exception e) {

        }
    }


    @Test
    public void searchCommoditiesByCategory() {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "category");
        input.put("searchValue", "cat 1");

        commodity.setId("1111");
        commodity.setName("Mobile");

        ArrayList<Commodity> returnValue = new ArrayList<>();
        returnValue.add(commodity);

        when(balootMock.filterCommoditiesByCategory("cat 1")).thenReturn(returnValue);

        try{
            mockMvc.perform(post("/commodities/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()", is(returnValue.size())))
                    .andExpect(jsonPath("$[0].id", is(returnValue.get(0).getId())))
                    .andExpect(jsonPath("$[0].name", is(returnValue.get(0).getName())));
        } catch (Exception e) {

        }
    }

    @Test
    public void searchCommoditiesByProvider() {
        String commodityId = "123";

        String searchBy = "provider";
        String searchValue = "user5";
        ArrayList<Commodity> test_commodities = new ArrayList<>();

        Commodity commodity = new Commodity();
        commodity.setId(commodityId);
        commodity.setName("product1 ");
        test_commodities.add(commodity);

        Map<String, String> testContent = new HashMap<>();
        testContent.put("searchOption", searchBy);
        testContent.put("searchValue",searchValue);


        when(balootMock.filterCommoditiesByProviderName(searchValue)).thenReturn(test_commodities);

        try {
            mockMvc.perform(post("/commodities/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(testContent)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(test_commodities.size()))
                    .andExpect(jsonPath("$[0].id").value(test_commodities.get(0).getId()))
                    .andExpect(jsonPath("$[0].name").value(test_commodities.get(0).getName()));
        } catch (Exception e) {

        }
    }

    @Test
    public void searchCommoditiesByDefault() {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "default");

        commodity.setId("1122");
        commodity.setName("Car");

        ArrayList<Commodity> returnValue = new ArrayList<>();
        returnValue.add(commodity);

        when(balootMock.filterCommoditiesByName(anyString())).thenReturn(returnValue);
        when(balootMock.filterCommoditiesByCategory(anyString())).thenReturn(new ArrayList<>());
        when(balootMock.filterCommoditiesByProviderName(anyString())).thenReturn(new ArrayList<>());

        try {
            mockMvc.perform(post("/commodities/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()", is(0)));
        } catch (Exception e) {

        }
    }

    @Test
    public void getSuggestedCommoditiesForAValidCommodity() throws NotExistentCommodity {
        commodity.setId("111");
        commodity.setName("Computer");

        Commodity commodity2 = new Commodity();
        commodity2.setId("222");
        commodity2.setName("Laptop");

        ArrayList<Commodity> returnValue = new ArrayList<>();
        returnValue.add(commodity2);

        when(balootMock.getCommodityById("111")).thenReturn(commodity);
        when(balootMock.suggestSimilarCommodities(commodity)).thenReturn(returnValue);

        try {
            mockMvc.perform(get("/commodities/" + "111" + "/suggested")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()", is(returnValue.size())))
                    .andExpect(jsonPath("$[0].id", is(returnValue.get(0).getId())))
                    .andExpect(jsonPath("$[0].name", is(returnValue.get(0).getName())))
                    .andExpect(jsonPath("$[1].id", is(returnValue.get(1).getId())))
                    .andExpect(jsonPath("$[1].name", is(returnValue.get(1).getName())));
        } catch (Exception e) {

        }
    }

    @Test
    public void getSuggestedCommoditiesForAnInvalidCommodity() throws NotExistentCommodity {
        when(balootMock.getCommodityById("111")).thenThrow(notExistentCommodity);
//        when(balootMock.suggestSimilarCommodities()).thenReturn(new ArrayList<>());

        try{
            mockMvc.perform(get("/commodities/" + "111" + "/suggested")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.length()").value(0));
        } catch (Exception e) {

        }
    }
}
