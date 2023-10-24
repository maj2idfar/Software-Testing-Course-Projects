package model;
import exceptions.CommodityIsNotInBuyList;
import exceptions.InsufficientCredit;
import exceptions.InvalidCreditRange;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

public class UserTest {
    private User user;

    @BeforeEach
    void init() {
        user = new User("testUser", "password", "test@example.com", "2000-01-01", "123 Main St");
    }

    @Test
    void AddNegativeCreditTest() {
        assertDoesNotThrow(() -> {
            user.addCredit(10);
        });
        assertEquals(10, user.getCredit());
        assertDoesNotThrow(() -> {
            user.addCredit(5);
        });
        assertEquals(15, user.getCredit());
        assertThrows(InvalidCreditRange.class, () -> {
            user.addCredit(-5);
        });
        assertEquals(15, user.getCredit());
    }

    @ParameterizedTest
    @ValueSource(floats = {0, 5, -5, 9, -10})
    public void AddCreditParameterizedTest(float amount){
        if(amount < 0) {
            assertThrows(InvalidCreditRange.class, () -> {
                user.addCredit(amount);
            });
            assertEquals(0, user.getCredit());
        } else {
            assertDoesNotThrow(() -> {
                user.addCredit(amount);
            });
            assertEquals(amount, user.getCredit());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "3, 2",
            "4, 5",
            "6, 0",
            "7, 9",
            "10, 2"
    })
    public void WithdrawCreditParameterizedTest(float toAdd, float toWithdraw){
        assertDoesNotThrow(() -> {
            user.addCredit(toAdd);
        });
        assertEquals(toAdd, user.getCredit());

        if(toWithdraw > toAdd) {
            assertThrows(InsufficientCredit.class, () -> {
                user.withdrawCredit(toWithdraw);
            });
            assertEquals(toAdd, user.getCredit());
        } else {
            assertDoesNotThrow(() -> {
                user.withdrawCredit(toWithdraw);
            });
            assertEquals(toAdd-toWithdraw, user.getCredit());
        }
    }

//    @ParameterizedTest
//    @ValueSource(floats = {3, 4, 5, 6, 9})
//    @Test
//    public void testAddWithdraw(float argument){
//        the_credit = user.GetCredit();
//        user.addCredit(argument);
//        user.withdrawCredit(argument);
//        assertEquals(the_credit,user.GetCredit());
//
//        assertDoesNotThrow(() -> {
//            user.addCredit(20);
//        });
//        assertEquals(20,user.getCredit());
//        assertDoesNotThrow(() -> {
//            user.withdrawCredit(2);
//        });
//        assertEquals(18,user.getCredit());
//        assertDoesNotThrow(() -> {
//            user.withdrawCredit(4);
//        });
//        assertEquals(14,user.getCredit());
//        assertDoesNotThrow(() -> {
//            commodity.updateInStock(-6);
//        });
//        assertEquals(8,commodity.getInStock());
//        assertThrows(NotInStock.class, () -> {
//            commodity.updateInStock(-10);
//        });
//    }


//    @Test
//    public void AddAndWithdrawTest(){
//        assertEquals(user.GetCredit()+10 , user.addCredit(10));
//        assertEquals(user.GetCredit()-8,user.withdrawCredit(8));
//        assertEquals(user.GetCredit()+3 , user.addCredit(3));
//        assertThrow(InsufficientCredit(),user.withdrawCredit(10));
//        assertThrow(InvalidCreditRange(),user.addCredit(-12));
//    }

    @Test
    public void AddBuyItemTest() {
        Commodity com1 = new Commodity(); com1.setId("1");
        Commodity com2 = new Commodity(); com2.setId("2");
        Commodity com3 = new Commodity(); com3.setId("3");

        user.addBuyItem(com1);
        assertEquals(1,user.getBuyList().get(com1));
        user.addBuyItem(com2);
        assertEquals(1,user.getBuyList().get(com2));
        user.addBuyItem(com1);
        assertEquals(2,user.getBuyList().get(com1));
        user.addBuyItem(com1);
        assertEquals(3,user.getBuyList().get(com1));
        user.addBuyItem(com2);
        assertEquals(2,user.getBuyList().get(com2));
        user.addBuyItem(com3);
        assertEquals(1,user.getBuyList().get(com3));
        assertEquals(3,user.getBuyList().get(com1));
    }

    @Test
    public void AddPurchasedItemWithPositiveQuantityTest(){
        String com1 = "1";
        String com2 = "2";
        String com3 = "3";

        user.addPurchasedItem(com1, 5);
        assertEquals(5,user.getPurchasedList().get(com1));
        user.addPurchasedItem(com2, 10);
        assertEquals(10,user.getPurchasedList().get(com2));
        user.addPurchasedItem(com1, 5);
        assertEquals(10,user.getPurchasedList().get(com1));
        user.addPurchasedItem(com1, 3);
        assertEquals(13,user.getPurchasedList().get(com1));
        user.addPurchasedItem(com2, 7);
        assertEquals(17,user.getPurchasedList().get(com2));
        user.addPurchasedItem(com3, 15);
        assertEquals(13,user.getPurchasedList().get(com1));
        assertEquals(17,user.getPurchasedList().get(com2));
        assertEquals(15,user.getPurchasedList().get(com3));
    }
    @Test
    public void AddPurchasedItemWithNegativeQuantityTest(){ // This should be considered, because negative quantity is not logical.
        String com1 = "1";
        String com2 = "2";
        String com3 = "3";

        user.addPurchasedItem(com1, 5);
        assertEquals(5,user.getPurchasedList().get(com1));
        user.addPurchasedItem(com2, 10);
        assertEquals(10,user.getPurchasedList().get(com2));
        user.addPurchasedItem(com1, -5);
        assertEquals(0,user.getPurchasedList().get(com1));
        user.addPurchasedItem(com1, 3);
        assertEquals(3,user.getPurchasedList().get(com1));
        user.addPurchasedItem(com2, 7);
        assertEquals(17,user.getPurchasedList().get(com2));
        user.addPurchasedItem(com3, -1);
        assertEquals(3,user.getPurchasedList().get(com1));
        assertEquals(17,user.getPurchasedList().get(com2));
        assertEquals(-1,user.getPurchasedList().get(com3));
    }

//    @Test
//    public void AddPurchasedItemTest2(){
//        Map<String, Integer> the_purchasedlist = new HashMap<>();
//        com1 = new Commodity();
//        assertEquals(the_buylist.put(com1.id,1),user.addBuyItem(com1));
//        com2 = new Commodity();
//        assertEquals(the_buylist.put(com2.id,1),user.addBuyItem(com2));
//        assertEquals(the_buylist.put(com1.id,2),user.addBuyItem(com1));
//        assertEquals(the_buylist.put(com1.id,3),user.addBuyItem(com1));
//        com3 = new Commodity();
//        assertEquals(the_buylist.put(com3.id,1),user.addBuyItem(com3));
//    }
    @Test
    public void RemoveInvalidItemFromBuyListTest(){
        Commodity com1 = new Commodity(); com1.setId("1");

        assertThrows(CommodityIsNotInBuyList.class, () -> {
            user.removeItemFromBuyList(com1);
        });
    }

    @Test
    public void RemoveValidItemFromBuyListTest(){
        Commodity com1 = new Commodity(); com1.setId("1");

        user.addBuyItem(com1);

        assertDoesNotThrow(() -> {
            user.removeItemFromBuyList(com1);
        });
    }

    @Test
    public void RemoveValidAndInvalidItemFromBuyListTest(){
        Commodity com1 = new Commodity(); com1.setId("1");
        Commodity com2 = new Commodity(); com2.setId("2");
        Commodity com3 = new Commodity(); com3.setId("3");
        Commodity com4 = new Commodity(); com4.setId("4");
        Commodity com5 = new Commodity(); com5.setId("5");

        user.addBuyItem(com1);
        user.addBuyItem(com2);

        assertDoesNotThrow(() -> {
            user.removeItemFromBuyList(com1);
        });

        user.addBuyItem(com3);

        assertDoesNotThrow(() -> {
            user.removeItemFromBuyList(com2);
        });

        user.addBuyItem(com4);

        assertThrows(CommodityIsNotInBuyList.class, () -> {
            user.removeItemFromBuyList(com5);
        });
    }

//    if(the_buylist.containsKey(id1)){
//        int q1 = the_buylist.get(id1);
//        if ( q1 == 1)
//            assertEquals(the_buylist.remove(id1),user.removeItemFromBuyList(com1));
//        else
//            assertEquals(the_buylist.put(id1,q1-1),user.removeItemFromBuyList(com1));
//
//    }
//        else

    //    public void testAddCredit(float amount) throws InvalidCreditRange {
//        if(amount < 0) {
//            // asserthrow (user.addCredit(amount), ...)
//        }
//    }

//    public void testaddCredit(float a , float c){
//        this.amount =a;
//        this.credit= c;
//        //assertThrows (InvalidCreditRange(),User.addCredit(-5))
//    }
}

