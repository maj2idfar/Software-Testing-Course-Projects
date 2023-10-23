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
    @ValueSource(ints = {3, 4, -5, -6, 9})
    public void testAddCredit(float amount) throws InvalidCreditRange {
        if(amount < 0) {
            // asserthrow (user.addCredit(amount), ...)
        }
    }



    @Test
    void testForAddNegativeAmount() {

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

