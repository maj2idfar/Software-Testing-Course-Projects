package project;

import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import model.Commodity;
import model.User;
import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
public class RunCucumberTest {
    private User user;
    private Commodity commodity1;
    private Commodity commodity2;
    private float starting_credit;
    private int commodity_quantity;
    Exception expppppp;

    // addCredit

    @Given("a user whose  account's credit status is saved in {string}")
    public void a_user_whose_account_s_credit_status_is_saved_in(String creditStatus) {
        user = new User("Ouldouz", "1234", "ouldouzzzz", "2002-03-23", "Tehran");
        user.setCredit(Float.parseFloat(creditStatus));
    }

    @When("the user adds negative {string} to his account's credit")
    public void the_user_adds_negative_to_his_account_s_credit(String creditAmount) {
        try {
            this.starting_credit = user.getCredit();
            float add_amount = Float.parseFloat(creditAmount);
            user.addCredit(add_amount);
        } catch (Exception exp) {
            expppppp = exp;
        }
    }

    @Then("the InvalidCreditRange exception massage {string} should be raised")
    public void the_InvalidCreditRange_exception_massage_should_be_raised(String exceptionMassage) {
        assertEquals(exceptionMassage, expppppp.getMessage());
    }

    @Then("user account credit should not be changed")
    public void user_account_credit_should_not_be_changed() {
        assertEquals(this.starting_credit, user.getCredit());
    }

    @When("the user adds non-negative {string} to his account's credit")
    public void the_user_adds_non_negative_to_his_account_s_credit(String creditAmount) {
        try {

            float add_amount = Float.parseFloat(creditAmount);
            user.addCredit(add_amount);
        } catch (Exception exp) {
            expppppp = exp;
        }
    }

    @Then("The user account credit {string} should be changed with the credit amount without exception")
    public void the_user_account_credit_should_be_changed_with_the_credit_amount_without_exception(String updatedCredit) {
        assertNull(expppppp);
        assertEquals(user.getCredit(), Float.parseFloat(updatedCredit));
    }

    // withdrawCredit

    @When("the user withdraw Insufficient {string} from his account's credit")
    public void the_user_withdraw_Insufficient_from_his_account_s_credit(String withdrawCreditAmount) {
        try {
            this.starting_credit = user.getCredit();
            float withdraw_amount = Float.parseFloat(withdrawCreditAmount);
            user.withdrawCredit(withdraw_amount);
        } catch (Exception exp) {
            expppppp = exp;
        }
    }

    @Then("the InsufficientCredit exception massage {string} should be raised")
    public void InsufficientCredit_exception_message_should_be_raised(String exceptionMessage) {
        assertEquals(exceptionMessage, expppppp.getMessage());
    }

    @When("the user withdraw Sufficient {string} from his account's credit")
    public void the_user_withdraw_Sufficient_from_his_account_s_credit(String withdrawCreditAmount) {
        try {
            this.starting_credit = user.getCredit();
            float withdraw_amount = Float.parseFloat(withdrawCreditAmount);
            user.withdrawCredit(withdraw_amount);
        } catch (Exception exp) {
            expppppp = exp;
        }
    }

    @Then("user account credit {string} should be changed with the credit amount without exception")
    public void user_account_credit_should_be_changed_with_the_credit_amount_without_exception(String updatedCredit) {
        assertNull(expppppp);
        assertEquals(user.getCredit(), Float.parseFloat(updatedCredit));
    }

    // removeFromBuyList

    @Given("a user with only-one-in-buy-list commodity")
    public void a_user_with_only_one_in_buy_list_commodity() {
        user = new User("Ouldouz", "1234", "Ouldouzzzz", "2002-03-23", "Tehran");
        commodity1 = new Commodity();
        commodity1.setId("1");
        user.addBuyItem(commodity1);
        commodity_quantity = user.getBuyList().get(commodity1.getId());
    }

    @When("the user remove the commodity from his buy-list")
    public void the_user_remove_the_commodity_from_their_buy_list() {
        try {
            user.removeItemFromBuyList(commodity1);
        } catch (Exception exp){
            expppppp = exp;
        }
    }

    @Then("the commodity should be deleted")
    public void the_commodity_should_be_deleted() {
        assertNull(expppppp);
        assertNull(user.getBuyList().get(commodity1.getId()));
    }

    @Given("a user with more-than-one-in-buy-list commodity")
    public void a_user_with_more_than_one_in_buy_list_commodity() {
        user = new User("Ouldouz", "1234", "Ouldouzzzz", "2002-03-23", "Tehran");
        commodity1 = new Commodity();
        commodity1.setId("1");
        commodity2 = new Commodity();
        commodity2.setId("1");
        user.addBuyItem(commodity1);
        user.addBuyItem(commodity2);
        commodity_quantity = user.getBuyList().get(commodity1.getId());
    }

    @Then("the commodity number should be updated")
    public void the_commodity_number_should_be_updated() {
        assertNull(expppppp);
        assertEquals(commodity_quantity -1 ,user.getBuyList().get(commodity1.getId()) );
    }

    @Given("a user with none-in-buy-list commodity")
    public void a_user_with_none_in_buy_list_commodity() {
        user = new User("Ouldouz", "1234", "Ouldouzzzz", "2002-03-23", "Tehran");
        commodity1 = new Commodity();
        commodity1.setId("1");
    }

    @Then("{string} exception message should be raised")
    public void the_exception_message_should_be_raised(String CommodityIsNotInBuyList) {
        assertEquals(CommodityIsNotInBuyList, expppppp.getMessage());
    }
}