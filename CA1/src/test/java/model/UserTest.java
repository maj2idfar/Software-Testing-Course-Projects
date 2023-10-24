package model;
import exceptions.InvalidCreditRange;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.runners;
//import org.junit.runners.Parameterized;
//import org.junit.runners.Parameterized.Parameters;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

public class UserTest {
//    @Test
//    public float amount , credit;

//    public UserTest();

    private User user;

    @BeforeAll
    static void initAll(){

    }

    @BeforeEach
    void init() {
        user = new User("testUser", "password", "test@example.com", "2000-01-01", "123 Main St");
    }

    @ParameterizedTest
    @ValueSource(floats = {3, 4, 5, 6, 9})
    public void testwithValueSource(float argument){
        assertEquals(argument+user.GetCredit(),user.addCredit(argument))
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


    @test
    public void testAddAndWithdraw(){
        assertEquals(user.GetCredit()+10 , user.addCredit(10));
        assertEquals(user.GetCredit()-8,user.withdrawCredit(8));
        assertEquals(user.GetCredit()+3 , user.addCredit(3));
        assertThrow(InsufficientCredit(),user.withdrawCredit(10));
        assertThrow(InvalidCreditRange(),user.addCredit(-12));

    }

    @test
    public void testAddbuyItem(){
        Map<String, Integer> the_buylist = new HashMap<>();
        com1 = new Commodity();
        assertEquals(the_buylist.put(com1.id,1),user.addBuyItem(com1));
        com2 = new Commodity();
        assertEquals(the_buylist.put(com2.id,1),user.addBuyItem(com2));
        assertEquals(the_buylist.put(com1.id,2),user.addBuyItem(com1));
        assertEquals(the_buylist.put(com1.id,3),user.addBuyItem(com1));
        com3 = new Commodity();
        assertEquals(the_buylist.put(com3.id,1),user.addBuyItem(com3));
    }

    @test
    public void testAddPurchasedItem(){
        Map<String, Integer> the_purchasedlist = new HashMap<>();
        com1 = new Commodity();
        assertEquals(the_buylist.put(com1.id,1),user.addBuyItem(com1));
        com2 = new Commodity();
        assertEquals(the_buylist.put(com2.id,1),user.addBuyItem(com2));
        assertEquals(the_buylist.put(com1.id,2),user.addBuyItem(com1));
        assertEquals(the_buylist.put(com1.id,3),user.addBuyItem(com1));
        com3 = new Commodity();
        assertEquals(the_buylist.put(com3.id,1),user.addBuyItem(com3));
    }
    @test
    public void testremoveItemFromBuyList(){
        Map<String, Integer> the_buylist = new HashMap<>();
        the_buylist= user.GetBuyList();
        com1 = new Commodity();
        int id1 = com1.getId();
        if(the_buylist.containsKey(id1)){
            int q1 = the_buylist.get(id1);
            if ( q1 == 1)
                assertEquals(the_buylist.remove(id1),user.removeItemFromBuyList(com1));
            else
                assertEquals(the_buylist.put(id1,q1-1),user.removeItemFromBuyList(com1));

        }
        else
            assertThrow(CommodityIsNotInBuyList,user.removeItemFromBuyList(com1))
        the_buylist.remove();

    }

    //    public void testAddCredit(float amount) throws InvalidCreditRange {
//        if(amount < 0) {
//            // asserthrow (user.addCredit(amount), ...)
//        }
//    }



    @Test
    void testForAddNegativeAmount() {
        assertEquals(10+ user.GetCredit(),user.addCredit(10));
        assertEquals(5 + user.GetCredit(),user.addCredit(5));
        assertThrow(InvalidCreditRange(),user.addCredit(-5));
        
    }

    @AfterEach
    void tearDown() {

    }

    @AfterAll
    void tearDownAll() {

    }

    public void testaddCredit(float a , float c){
        this.amount =a;
        this.credit= c;
        //assertThrows (InvalidCreditRange(),User.addCredit(-5))
    }
    @ParameterizedTest
    public static Collection<Object [] >parameters()
    {
       return Arrays.asList ( new )
    }

}

