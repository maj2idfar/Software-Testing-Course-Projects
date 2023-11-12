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
        engine = new Engine();
        int i =5;
        assertEquals(0,engine.getAverageOrderQuantityByCustomer(5));
    }

    @Test
    public void testGetAverageOrderQuantityByCustomerNonEmpty(){
        engine = new Engine();
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

        assertEquals(2,engine.getAverageOrderQuantityByCustomer(i));
    }

    @Test
    public void testGetQuantityPatternByPrice1() {
        engine = new Engine();
        assertEquals(0, engine.getQuantityPatternByPrice(200));
    }

    @Test
    public void testGetQuantityPatternByPrice2() {
        engine = new Engine();

        Order O1 = new Order();
        O1.setId(1);
        O1.setCustomer(4);
        O1.setQuantity(2);
        O1.setPrice(200);

        Order O2 = new Order();
        O2.setId(2);
        O2.setCustomer(4);
        O2.setQuantity(3);
        O2.setPrice(200);

        Order O3 = new Order();
        O3.setId(3);
        O3.setCustomer(4);
        O3.setQuantity(4);
        O3.setPrice(200);

        engine.orderHistory.add(O1);
        engine.orderHistory.add(O2);
        engine.orderHistory.add(O3);

        assertEquals(1, engine.getQuantityPatternByPrice(200));
    }

    @Test
    public void testGetQuantityPatternByPrice3() {
        engine = new Engine();

        Order O1 = new Order();
        O1.setId(1);
        O1.setCustomer(4);
        O1.setQuantity(2);
        O1.setPrice(200);

        Order O2 = new Order();
        O2.setId(2);
        O2.setCustomer(4);
        O2.setQuantity(3);
        O2.setPrice(200);

        Order O3 = new Order();
        O3.setId(1);
        O3.setCustomer(4);
        O3.setQuantity(4);
        O3.setPrice(200);

        engine.orderHistory.add(O1);
        engine.orderHistory.add(O2);
        engine.orderHistory.add(O3);

        assertEquals(1, engine.getQuantityPatternByPrice(200));
    }

    @Test
    public void testGetQuantityPatternByPrice4() {
        engine = new Engine();

        Order O1 = new Order();
        O1.setId(1);
        O1.setCustomer(4);
        O1.setQuantity(2);
        O1.setPrice(200);

        Order O2 = new Order();
        O2.setId(2);
        O2.setCustomer(4);
        O2.setQuantity(3);
        O2.setPrice(100);

        Order O3 = new Order();
        O3.setId(3);
        O3.setCustomer(4);
        O3.setQuantity(4);
        O3.setPrice(200);

        engine.orderHistory.add(O1);
        engine.orderHistory.add(O2);
        engine.orderHistory.add(O3);

        assertEquals(2, engine.getQuantityPatternByPrice(200));
    }

    @Test
    public void testGetQuantityPatternByPrice5() {
        engine = new Engine();

        Order O1 = new Order();
        O1.setId(1);
        O1.setCustomer(4);
        O1.setQuantity(2);
        O1.setPrice(200);

        Order O2 = new Order();
        O2.setId(2);
        O2.setCustomer(4);
        O2.setQuantity(3);
        O2.setPrice(200);

        Order O3 = new Order();
        O3.setId(3);
        O3.setCustomer(4);
        O3.setQuantity(5);
        O3.setPrice(200);

        engine.orderHistory.add(O1);
        engine.orderHistory.add(O2);
        engine.orderHistory.add(O3);

        assertEquals(0, engine.getQuantityPatternByPrice(200));
    }

    public void testGetCustomerFraudulentQuantityEmptyOrder(){
        engine = new Engine();
        int i = 3;
        Order O6 = new Order();
        O6.setId(6);
        O6.setCustomer(i);
        O6.setQuantity(12);
        O6.setPrice(250);

        assertEquals(12,engine.getCustomerFraudulentQuantity(O6));
    }
    public void testGetCustomerFraudulentQuantityEmptyNonOrder1(){
        engine = new Engine();

        int i = 4;

        Order O6 = new Order();
        O6.setId(6);
        O6.setCustomer(i);
        O6.setQuantity(6);
        O6.setPrice(250);

        Order O1 = new Order();
        O1.setId(1);
        O1.setCustomer(i);
        O1.setQuantity(2);
        O1.setPrice(1000);

        Order O2 = new Order();
        O2.setId(2);
        O2.setCustomer(i);
        O2.setQuantity(4);
        O2.setPrice(200);

        Order O3 = new Order();
        O3.setId(3);
        O3.setCustomer(i);
        O3.setQuantity(1);
        O3.setPrice(3200);

        Order O4 = new Order();
        O4.setId(4);
        O4.setCustomer(i+1);
        O4.setQuantity(10);
        O4.setPrice(20);

        Order O5 = new Order();
        O5.setId(5);
        O5.setCustomer(i);
        O5.setQuantity(1);
        O5.setPrice(2546);

        engine.orderHistory.add(O1);
        engine.orderHistory.add(O2);
        engine.orderHistory.add(O3);
        engine.orderHistory.add(O4);
        engine.orderHistory.add(O5);

        assertEquals(4,engine.getCustomerFraudulentQuantity(O6));

    }

    public void testGetCustomerFraudulentQuantityEmptyNonOrder2(){
        engine = new Engine();

        int i = 3;
        Order O6 = new Order();
        O6.setId(6);
        O6.setCustomer(i);
        O6.setQuantity(1);
        O6.setPrice(250);

        Order O1 = new Order();
        O1.setId(1);
        O1.setCustomer(i);
        O1.setQuantity(2);
        O1.setPrice(1000);

        Order O2 = new Order();
        O2.setId(2);
        O2.setCustomer(i);
        O2.setQuantity(4);
        O2.setPrice(200);

        Order O3 = new Order();
        O3.setId(3);
        O3.setCustomer(i);
        O3.setQuantity(1);
        O3.setPrice(3200);

        Order O4 = new Order();
        O4.setId(4);
        O4.setCustomer(i+1);
        O4.setQuantity(10);
        O4.setPrice(20);

        Order O5 = new Order();
        O5.setId(5);
        O5.setCustomer(i);
        O5.setQuantity(1);
        O5.setPrice(2546);

        engine.orderHistory.add(O1);
        engine.orderHistory.add(O2);
        engine.orderHistory.add(O3);
        engine.orderHistory.add(O4);
        engine.orderHistory.add(O5);

        assertEquals(0,engine.getCustomerFraudulentQuantity(O6));

    }

    @Test
    public void testAddOrderAndGetFraudulentQuantity1() {
        engine = new Engine();
        Order o = new Order();
        assertEquals(0, engine.addOrderAndGetFraudulentQuantity(o));
    }

    @Test
    public void testAddOrderAndGetFraudulentQuantity2() {
        engine = new Engine();

        Order O1 = new Order();
        O1.setId(1);
        O1.setCustomer(4);
        O1.setQuantity(5);
        O1.setPrice(200);

        engine.orderHistory.add(O1);

        Order O2 = new Order();
        O2.setId(2);
        O2.setCustomer(4);
        O2.setQuantity(10);
        O2.setPrice(200);

        engine.orderHistory.add(O1);
        engine.orderHistory.add(O2);

        assertEquals(0, engine.addOrderAndGetFraudulentQuantity(O1));
    }

    @Test
    public void testAddOrderAndGetFraudulentQuantity3() {
        engine = new Engine();

        int i = 4;

        Order O6 = new Order();
        O6.setId(6);
        O6.setCustomer(i);
        O6.setQuantity(6);
        O6.setPrice(250);

        Order O1 = new Order();
        O1.setId(1);
        O1.setCustomer(i);
        O1.setQuantity(2);
        O1.setPrice(1000);

        Order O2 = new Order();
        O2.setId(2);
        O2.setCustomer(i);
        O2.setQuantity(4);
        O2.setPrice(200);

        Order O3 = new Order();
        O3.setId(3);
        O3.setCustomer(i);
        O3.setQuantity(1);
        O3.setPrice(3200);

        Order O4 = new Order();
        O4.setId(4);
        O4.setCustomer(i+1);
        O4.setQuantity(10);
        O4.setPrice(20);

        Order O5 = new Order();
        O5.setId(5);
        O5.setCustomer(i);
        O5.setQuantity(1);
        O5.setPrice(2546);

        engine.orderHistory.add(O1);
        engine.orderHistory.add(O2);
        engine.orderHistory.add(O3);
        engine.orderHistory.add(O4);
        engine.orderHistory.add(O5);

        assertEquals(4, engine.addOrderAndGetFraudulentQuantity(O6));
    }

}