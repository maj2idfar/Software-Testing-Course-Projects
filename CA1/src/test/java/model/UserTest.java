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

