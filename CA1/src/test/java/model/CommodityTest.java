package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CommodityTest {
    private Commodity commodity;

    @BeforeAll
    public void initAll() {
        comd = new Commodity();
    }

    @BeforeEach
    public void initEach() {
        comd.SetId("1");
        comd.SetName("the_comd");
        comd.SetProviderId("123");
        comd.SetPrice(1000);
        comd.SetCategories(ArrayList<>(1,2,3,4))
        comd.SetRating(8);
        comd.SetImage("1111111");
        comd.SetUserRate(HashMap<M>());
        comd.SetInitRate(0);

    }

    @Test
    public void OrdinaryupdateInStockTest() {
        assertEquals(10,comd.updateInStock(10));
        assertEquals(18,comd.updateInStock(8));
        assertEquals(12,comd.updateInStock(-6));
        assertEquals(15,comd.updateInStock(3));
        assertEquals(10,comd.updateInStock(-5));
        assertEquals(5,comd.updateInStock(-5));
    }

    public void NegValupdateInStockTest() {
        assertEquals(20,comd.updateInStock(20));
        assertEquals(18,comd.updateInStock(-2));
        assertEquals(14,comd.updateInStock(-4));
        assertEquals(8,comd.updateInStock(-6));
        assertThrow(new NotInStock,comd.updateInStock(-10));
    }

    public void ZeroValueInStockTest(){
        assertEquals(20,comd.updateInStock(20));
        assertEquals(18,comd.updateInStock(-2));
        assertEquals(14,comd.updateInStock(-4));
        assertEquals(8,comd.updateInStock(-6));
        assertEquals(0,comd.updateInStock(-8));
    }

    public void ratingTest(){
        comd.addRate("user1",9);
        comd.addRate("user2",8.5);
        comd.addRate("user3",3);
        comd.addRate("user4",1);
        assertEquals(4.3,comd.calcRating())
    }

    @Test
    public void addRateTest() {
        // addRate('user1', 30)
        // addRate('user2', 30)
        // addRate('user3', 30)
        // addRate('user2', 40)

        // assertEquals(dasti, getRating)
    }
}
