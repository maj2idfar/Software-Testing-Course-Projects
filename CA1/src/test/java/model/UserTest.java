package model;
import exceptions.CommodityIsNotInBuyList;
import exceptions.InvalidCreditRange;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.runners;
//import org.junit.runners.Parameterized;
//import org.junit.runners.Parameterized.Parameters;
import static org.junit.jupiter.api.Assertions.*;

import exceptions.NotInStock;
import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

public class UserTest {
//    @Test
//    public float amount , credit;

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
    @ValueSource(floats = {3, 4, 5, 6, 9})
    public void testwithValueSource(float argument){
        user.addCredit(argument)
        assertEquals(argument+user.getCredit(),user.addCredit(argument))
    }

    @ParameterizedTest
    @ValueSource(floats = {3, 4, -5, -6, 9})
    public void testwithNegValueSource(float argument){
        if ( argument < 0 ){
            assertThrow(InvalidCreditRange(),user.addCredit(argument))
        }
    }

    @ParameterizedTest
    @ValueSource(floats = {3, 4, -5, -6, 9})
    public void testwithdrawValueSource(float argument){
        if ( argument > user.GetCredit() ){
            assertThrow(InsufficientCredit(),user.addCredit(argument))
        }
        else
            assertEquals(user.GetCredit()-argument,user.withdrawCrdit(argument))
    }

    @ParameterizedTest
    @ValueSource(floats = {3, 4, 5, 6, 9})
    public void testAddWithdraw(float argument){
        the_credit = user.GetCredit();
        user.addCredit(argument);
        user.withdrawCredit(argument);
        assertEquals(the_credit,user.GetCredit())
    }


    @Test
    public void testAddAndWithdraw(){
        assertEquals(user.GetCredit()+10 , user.addCredit(10));
        assertEquals(user.GetCredit()-8,user.withdrawCredit(8));
        assertEquals(user.GetCredit()+3 , user.addCredit(3));
        assertThrow(InsufficientCredit(),user.withdrawCredit(10));
        assertThrow(InvalidCreditRange(),user.addCredit(-12));

    }

    @Test
    public void AddBuyItemTest(){
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
    public void AddPurchasedItemWithNegativeQuantityTest(){ // This should be considered.
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
        assertEquals(7,user.getPurchasedList().get(com2));
        user.addPurchasedItem(com3, -1);
        assertEquals(3,user.getPurchasedList().get(com1));
        assertEquals(7,user.getPurchasedList().get(com2));
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

    public void testaddCredit(float a , float c){
        this.amount =a;
        this.credit= c;
        //assertThrows (InvalidCreditRange(),User.addCredit(-5))
    }
}

