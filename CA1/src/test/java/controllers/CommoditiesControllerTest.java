package controllers;

import exceptions.NotExistentComment;
import exceptions.NotExistentCommodity;
import model.Commodity;
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
    @Mock private NotExistentCommodity notExistentCommodity;
    @Mock private NumberFormatException numberFormatException;

    private CommoditiesController commoditiesController;

    @BeforeEach
    public void setUp() {
        baloot = mock(Baloot.class);
        commodity = mock(Commodity.class);

        commoditiesController = new CommoditiesController();
        commoditiesController.setBaloot(baloot);
    }

//    @Test
//    public void getCommoditiesTest() {
//        ArrayList<Commodity> commodities;
//        commodities.add()
//        when(baloot.getCommodities()).thenReturn();
//    }

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
}
