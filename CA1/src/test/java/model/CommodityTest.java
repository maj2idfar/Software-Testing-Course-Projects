package model;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.NotInStock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashMap;

public class CommodityTest {
    private Commodity commodity;

    @BeforeAll
    public void initAll() {
        commodity = new Commodity();
    }

    @BeforeEach
    public void initEach() {
        commodity.setId("1");
        commodity.setName("the_comd");
        commodity.setProviderId("123");
        commodity.setPrice(1000);
        ArrayList<String> categories = new ArrayList<>();
        categories.add("1");
        categories.add("2");
        categories.add("3");
        categories.add("4");
        commodity.setCategories(categories);
        commodity.setRating(8);
        commodity.setImage("1111111");
        commodity.setUserRate(new HashMap <String, Integer>());
        commodity.setInitRate(0);
    }

    @Test
    public void OrdinaryUpdateInStockTest() {
        assertDoesNotThrow(() -> {
            commodity.updateInStock(10);
        });
        assertEquals(10,commodity.getInStock());
        assertDoesNotThrow(() -> {
            commodity.updateInStock(8);
        });
        assertEquals(18,commodity.getInStock());
        assertDoesNotThrow(() -> {
            commodity.updateInStock(-6);
        });
        assertEquals(12,commodity.getInStock());
        assertDoesNotThrow(() -> {
            commodity.updateInStock(3);
        });
        assertEquals(15,commodity.getInStock());
        assertDoesNotThrow(() -> {
            commodity.updateInStock(-5);
        });
        assertEquals(10,commodity.getInStock());
        assertDoesNotThrow(() -> {
            commodity.updateInStock(-5);
        });
        assertEquals(5,commodity.getInStock());
    }
    @Test
    public void NegValueUpdateInStockTest() {
        assertDoesNotThrow(() -> {
            commodity.updateInStock(20);
        });
        assertEquals(20,commodity.getInStock());
        assertDoesNotThrow(() -> {
            commodity.updateInStock(-2);
        });
        assertEquals(18,commodity.getInStock());
        assertDoesNotThrow(() -> {
            commodity.updateInStock(-4);
        });
        assertEquals(14,commodity.getInStock());
        assertDoesNotThrow(() -> {
            commodity.updateInStock(-6);
        });
        assertEquals(8,commodity.getInStock());
        assertThrows(NotInStock.class, () -> {
            commodity.updateInStock(-10);
        });
    }

    @Test
    public void ZeroValueInStockTest(){
        assertDoesNotThrow(() -> {
            commodity.updateInStock(20);
        });
        assertEquals(20,commodity.getInStock());
        assertDoesNotThrow(() -> {
            commodity.updateInStock(-2);
        });
        assertEquals(18,commodity.getInStock());
        assertDoesNotThrow(() -> {
            commodity.updateInStock(-4);
        });
        assertEquals(14,commodity.getInStock());
        assertDoesNotThrow(() -> {
            commodity.updateInStock(-6);
        });
        assertEquals(8,commodity.getInStock());
        assertDoesNotThrow(() -> {
            commodity.updateInStock(-8);
        });
        assertEquals(0,commodity.getInStock());
    }

    @Test
    public void OrdinaryRatingTest(){
        commodity.addRate("user1",9);
        commodity.addRate("user2",8);
        commodity.addRate("user3",3);
        commodity.addRate("user4",1);
        assertEquals(4.2,commodity.getRating());
    }

    @Test
    public void SomeoneChangesTheirRateRatingTest() {
        commodity.addRate("user1",30);
        commodity.addRate("user2",9);
        commodity.addRate("user3",21);
        commodity.addRate("user2",15);
        assertEquals(16.5,commodity.getRating());
    }
}
