package model;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.NotInStock;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashMap;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
        commodity.setInStock(0);
        commodity.setImage("1111111");
        commodity.setUserRate(new HashMap <String, Integer>());
        commodity.setInitRate(0);
    }

    @ParameterizedTest
    @ValueSource (ints = {1, 10, 100, 1000, 10000})
    public void PositiveUpdateInStockTest(int amount) {
        assertDoesNotThrow(() -> {
            commodity.updateInStock(amount);
        });
    }

    @ParameterizedTest
    @ValueSource (ints = {-1, -10, -100, -1000, -10000})
    public void NegativeUpdateInStockTest(int amount) {
        assertThrows(NotInStock.class, () -> {
            commodity.updateInStock(amount);
        });
    }

    @Test
    public void ZeroUpdateInStockTest() {
        assertDoesNotThrow(() -> {
            commodity.updateInStock(0);
        });
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

    @ParameterizedTest
    @CsvSource({
            "\"user1\", 9",
            "\"user2\", 10",
            "\"user3\", 0",
            "\"user4\", -5",
            "\"user5\", -6",
    })
    public void addRateTest(String user_name, int score) {
        commodity.addRate(user_name,score);
        assertEquals((float)((float)score)/2,commodity.getRating());
    }

    @ParameterizedTest
    @CsvSource({
            "\"user1\", 9, 10",
            "\"user2\", 10, 4",
            "\"user3\", 0, 5",
            "\"user4\", -5, 12",
            "\"user5\", -6, -8",
    })
    public void ChangeRateTest(String user_name, int score1, int score2) {
        commodity.addRate(user_name,score1);
        assertEquals((float)((float)score1)/2,commodity.getRating());
        commodity.addRate(user_name, score2);
        assertEquals((float)((float)score2)/2,commodity.getRating());
    }

    @Test
    public void OrdinaryRatingTest() {
        commodity.addRate("user1",9);
        commodity.addRate("user2",8);
        commodity.addRate("user3",2);
        commodity.addRate("user4",1);
        assertEquals(4,commodity.getRating());
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
