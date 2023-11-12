package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EngineTest {

    private Engine engine;

    @BeforeEach
    public void setUp(){
        engine = new Engine();
    }

    @Test
    public void testGetAverageOrderQuantityByCustomerEmpty(){
        int i =5;
        assertEquals(0,engine.getAverageOrderQuantityByCustomer(5));
    }

    @Test
    public void testgetAverageOrderQuantityByCustomerNonEmpty(){
        int i = 4;

        Order O1 = new Order();
        O1.setId(1);
        O1.setCustomer(i);
        O1.setQuantity(2);
        O1.setPrice(1000);

        Order O2 = new Order();
        O2.setId(2);
        O2.setCustomer(i);
        O2.setQuantity(5);
        O2.setPrice(200);

        Order O3 = new Order();
        O3.setId(1);
        O3.setCustomer(i);
        O3.setQuantity(1);
        O3.setPrice(3200);

        Order O4 = new Order();
        O4.setId(1);
        O4.setCustomer(i+1);
        O4.setQuantity(10);
        O4.setPrice(20);

        Order O5 = new Order();
        O5.setId(1);
        O5.setCustomer(i);
        O5.setQuantity(2);
        O5.setPrice(2546);

        engine.orderHistory.add(O1);
        engine.orderHistory.add(O2);
        engine.orderHistory.add(O3);
        engine.orderHistory.add(O4);
        engine.orderHistory.add(O5);

        assertEquals(2.5,engine.getAverageOrderQuantityByCustomer(i));
    }


    @Test
    public void justAnExample() {
        // TODO
    }

}