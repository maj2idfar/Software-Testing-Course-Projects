package controllers;

import exceptions.NotExistentComment;
import exceptions.NotExistentCommodity;
import exceptions.NotExistentUser;
import model.Comment;
import model.Commodity;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.configuration.IMockitoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.Baloot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class CommoditiesControllerTest {
    @Mock private Baloot baloot;
    @Mock private Commodity commodity;
    @Mock private User user;
    @Mock private NotExistentCommodity notExistentCommodity;
    @Mock private NumberFormatException numberFormatException;
    @Mock private NotExistentUser notExistentUser;

    private CommoditiesController commoditiesController;

    @BeforeEach
    public void setUp() {
        baloot = mock(Baloot.class);
        commodity = mock(Commodity.class);

        commoditiesController = new CommoditiesController();
        commoditiesController.setBaloot(baloot);
    }

    @Test
    public void getCommoditiesTest() {
        ArrayList<Commodity> returnValue = new ArrayList<>();
        returnValue.add(commodity);

        when(baloot.getCommodities()).thenReturn(returnValue);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.getCommodities();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(returnValue, response.getBody());
    }

    @Test
    public void getExistingCommodity() throws NotExistentCommodity {
        when(baloot.getCommodityById(anyString())).thenReturn(new Commodity());

        ResponseEntity<Commodity> response = commoditiesController.getCommodity("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Commodity);

        verify(baloot, times(1)).getCommodityById("1");
    }

    @Test
    public void getInvalidCommodity() throws NotExistentCommodity {
        notExistentCommodity = mock(NotExistentCommodity.class);

        when(baloot.getCommodityById(anyString())).thenThrow(notExistentCommodity);
        when(notExistentCommodity.getMessage()).thenReturn("Commodity doesn't exist!");

        ResponseEntity<Commodity> response = commoditiesController.getCommodity("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() == null);

        verify(baloot, times(1)).getCommodityById("1");
    }

    @Test
    public void rateExistingCommodityWithValidRate() throws NotExistentCommodity {
        Map<String, String> input = new HashMap<>();
        input.put("username", "user");
        input.put("rate", "10");

        when(baloot.getCommodityById(anyString())).thenReturn(commodity);
        doNothing().when(commodity).addRate(anyString(), anyInt());

        ResponseEntity<String> response = commoditiesController.rateCommodity("1", input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("rate added successfully!", response.getBody());

        verify(baloot, times(1)).getCommodityById("1");
        verify(commodity, times(1)).addRate("user", 10);
    }

    @Test
    public void rateInvalidCommodityWithValidRate() throws NotExistentCommodity {
        notExistentCommodity = mock(NotExistentCommodity.class);

        Map<String, String> input = new HashMap<>();
        input.put("username", "user");
        input.put("rate", "10");

        when(baloot.getCommodityById(anyString())).thenThrow(notExistentCommodity);
        doNothing().when(commodity).addRate(anyString(), anyInt());
        when(notExistentCommodity.getMessage()).thenReturn("Commodity doesn't exist!");

        ResponseEntity<String> response = commoditiesController.rateCommodity("1", input);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Commodity doesn't exist!", response.getBody());

        verify(baloot, times(1)).getCommodityById("1");
        verify(commodity, never()).addRate(anyString(), anyInt());
    }

    @Test
    public void rateExistingCommodityWithInvalidRate() throws NumberFormatException, NotExistentCommodity {
        numberFormatException = mock(NumberFormatException.class);

        Map<String, String> input = new HashMap<>();
        input.put("username", "user");
        input.put("rate", "10");

        when(baloot.getCommodityById(anyString())).thenReturn(commodity);
        doThrow(numberFormatException).when(commodity).addRate(anyString(), anyInt());
        when(numberFormatException.getMessage()).thenReturn("Number format is incorrect!");

        ResponseEntity<String> response = commoditiesController.rateCommodity("1", input);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Number format is incorrect!", response.getBody());

        verify(baloot, times(1)).getCommodityById("1");
        verify(commodity, times(1)).addRate("user", 10);
    }

    @Test
    public void addCommentForExistingCommodity() throws NotExistentUser {
        user = mock(User.class);

        when(baloot.generateCommentId()).thenReturn(1);
        when(baloot.getUserById(anyString())).thenReturn(user);
        when(user.getEmail()).thenReturn("user@test.com");
        when(user.getUsername()).thenReturn("user");
        doNothing().when(baloot).addComment(isA(Comment.class));

        Map<String, String> input = new HashMap<>();
        input.put("username", "user");
        input.put("comment", "This is a test comment!");

        ResponseEntity<String> response = commoditiesController.addCommodityComment("1", input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("comment added successfully!", response.getBody());

        verify(baloot, times(1)).generateCommentId();
        verify(baloot, times(1)).getUserById("user");
        verify(user, times(1)).getEmail();
        verify(user, times(1)).getUsername();
        verify(baloot, times(1)).addComment(isA(Comment.class));
    }

    @Test
    public void addCommentForInvalidCommodity() throws NotExistentUser {
        user = mock(User.class);
        notExistentUser = mock(NotExistentUser.class);

        when(baloot.generateCommentId()).thenReturn(1);
        when(baloot.getUserById(anyString())).thenThrow(notExistentUser);
        when(user.getEmail()).thenReturn("user@test.com");
        when(user.getUsername()).thenReturn("user");
        doNothing().when(baloot).addComment(isA(Comment.class));
        when(notExistentUser.getMessage()).thenReturn("User not found!");

        Map<String, String> input = new HashMap<>();
        input.put("username", "user");
        input.put("comment", "This is a test comment!");

        ResponseEntity<String> response = commoditiesController.addCommodityComment("1", input);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(notExistentUser.getMessage(), response.getBody());

        verify(baloot, times(1)).generateCommentId();
        verify(baloot, times(1)).getUserById("user");
        verify(user, never()).getEmail();
        verify(user, never()).getUsername();
        verify(baloot, never()).addComment(isA(Comment.class));
    }
<<<<<<< HEAD
    
=======

<<<<<<< HEAD
>>>>>>> c7eb86e8725978876811bcfa2e5ab24e3d72eed2
    @Test
    public void getCommodityCommentTest() {
        ArrayList<Comment> returnValue = new ArrayList<>();
        returnValue.add(new Comment());

        when(baloot.getCommentsForCommodity(anyInt())).thenReturn(returnValue);

        ResponseEntity<ArrayList<Comment>> response = commoditiesController.getCommodityComment("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(returnValue, response.getBody());
    }
<<<<<<< HEAD
=======
=======
    // getCommodityComment
>>>>>>> 54fed2260e8cdbf99ae5fa2907431f6fd6fd7cea
>>>>>>> c7eb86e8725978876811bcfa2e5ab24e3d72eed2

    @Test
    public void searchCommoditiesByName() {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "name");
        input.put("searchValue", "com 1");

        ArrayList<Commodity> returnValue = new ArrayList<>();
        returnValue.add(commodity);

        when(baloot.filterCommoditiesByName(anyString())).thenReturn(returnValue);
        when(baloot.filterCommoditiesByCategory(anyString())).thenReturn(new ArrayList<>());
        when(baloot.filterCommoditiesByProviderName(anyString())).thenReturn(new ArrayList<>());

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.searchCommodities(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ArrayList<Commodity>);
        assertEquals(returnValue, response.getBody());

        verify(baloot, times(1)).filterCommoditiesByName("com 1");
        verify(baloot, never()).filterCommoditiesByCategory(anyString());
        verify(baloot, never()).filterCommoditiesByProviderName(anyString());
    }

    @Test
    public void searchCommoditiesByCategory() {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "category");
        input.put("searchValue", "cat 1");

        ArrayList<Commodity> returnValue = new ArrayList<>();
        returnValue.add(commodity);

        when(baloot.filterCommoditiesByName(anyString())).thenReturn(new ArrayList<>());
        when(baloot.filterCommoditiesByCategory(anyString())).thenReturn(returnValue);
        when(baloot.filterCommoditiesByProviderName(anyString())).thenReturn(new ArrayList<>());

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.searchCommodities(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ArrayList<Commodity>);
        assertEquals(returnValue, response.getBody());

        verify(baloot, never()).filterCommoditiesByName(anyString());
        verify(baloot, times(1)).filterCommoditiesByCategory("cat 1");
        verify(baloot, never()).filterCommoditiesByProviderName(anyString());
    }

    @Test
    public void searchCommoditiesByProvider() {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "provider");
        input.put("searchValue", "pro 1");

        ArrayList<Commodity> returnValue = new ArrayList<>();
        returnValue.add(commodity);

        when(baloot.filterCommoditiesByName(anyString())).thenReturn(new ArrayList<>());
        when(baloot.filterCommoditiesByCategory(anyString())).thenReturn(new ArrayList<>());
        when(baloot.filterCommoditiesByProviderName(anyString())).thenReturn(returnValue);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.searchCommodities(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ArrayList<Commodity>);
        assertEquals(returnValue, response.getBody());

        verify(baloot, never()).filterCommoditiesByName(anyString());
        verify(baloot, never()).filterCommoditiesByCategory(anyString());
        verify(baloot, times(1)).filterCommoditiesByProviderName("pro 1");
    }

    @Test
    public void searchCommoditiesByDefault() {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "default");

        ArrayList<Commodity> returnValue = new ArrayList<>();
        returnValue.add(commodity);

        when(baloot.filterCommoditiesByName(anyString())).thenReturn(returnValue);
        when(baloot.filterCommoditiesByCategory(anyString())).thenReturn(new ArrayList<>());
        when(baloot.filterCommoditiesByProviderName(anyString())).thenReturn(new ArrayList<>());

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.searchCommodities(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ArrayList<Commodity>);
        assertTrue(response.getBody().size() == 0);

        verify(baloot, never()).filterCommoditiesByName(anyString());
        verify(baloot, never()).filterCommoditiesByCategory(anyString());
        verify(baloot, never()).filterCommoditiesByProviderName(anyString());
    }

    @Test
    public void getSuggestedCommoditiesForAValidCommodity() throws NotExistentCommodity {
        ArrayList<Commodity> returnValue = new ArrayList<>();
        returnValue.add(commodity);

        when(baloot.getCommodityById(anyString())).thenReturn(commodity);
        when(baloot.suggestSimilarCommodities(isA(Commodity.class))).thenReturn(returnValue);

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.getSuggestedCommodities("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ArrayList<Commodity>);
        assertEquals(returnValue, response.getBody());

        verify(baloot, times(1)).getCommodityById("1");
        verify(baloot, times(1)).suggestSimilarCommodities(isA(Commodity.class));
    }

    @Test
    public void getSuggestedCommoditiesForAnInvalidCommodity() throws NotExistentCommodity {
        notExistentCommodity = mock(NotExistentCommodity.class);

        when(baloot.getCommodityById(anyString())).thenThrow(notExistentCommodity);
        when(baloot.suggestSimilarCommodities(isA(Commodity.class))).thenReturn(new ArrayList<>());

        ResponseEntity<ArrayList<Commodity>> response = commoditiesController.getSuggestedCommodities("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ArrayList<Commodity>);
        assertTrue(response.getBody().size() == 0);

        verify(baloot, times(1)).getCommodityById("1");
        verify(baloot, never()).suggestSimilarCommodities(isA(Commodity.class));
    }
}
