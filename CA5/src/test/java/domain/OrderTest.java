package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {
    @Test
    public void testOrderIdGetter() {
        Order order = new Order();

        order.setId(5);

        assertEquals(5, order.getId());
    }

    @Test
    public void testOrderPriceGetter() {
        Order order = new Order();

        order.setPrice(5);

        assertEquals(5, order.getPrice());
    }

    @Test
    public void testOrderCustomerGetter() {
        Order order = new Order();

        order.setCustomer(5);

        assertEquals(5, order.getCustomer());
    }

    @Test
    public void testOrderQuantity() {
        Order order = new Order();

        order.setQuantity(5);

        assertEquals(5, order.getQuantity());
    }

    @Test
    public void testOrderEqualsElse() {
        Order order = new Order();
        order.setId(5);

        Object order2 = new Object();

        assertEquals(false, order.equals(order2));
    }

    @Test
    public void testOrderEqualsIf() {
        Order order = new Order();
        order.setId(5);

        Order order2 = new Order();
        order2.setId(6);

        assertEquals(false, order.equals(order2));
    }
}
