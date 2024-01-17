Feature: Credit Adding
    Scenario Outline: Adding negative credit
        Given a user whose  account's credit status is saved in "<creditStatus>"
        When the user adds negative "<creditAmount>" to his account's credit
        Then the InvalidCreditRange exception massage "<exceptionMassage>" should be raised
        And user account credit should not be changed

        Examples:
            | creditStatus | creditAmount | exceptionMassage |
            | 10           | -50          | Credit value must be a positive float |
            | 0            | -10          | Credit value must be a positive float |
            | 1000         | -20          | Credit value must be a positive float |

    Scenario Outline: Adding non-negative credit
        Given a user whose  account's credit status is saved in "<creditStatus>"
        When the user adds non-negative "<creditAmount>" to his account's credit
        Then user account credit "<updatedCredit>" should be changed with the credit amount without exception

        Examples:
            | creditStatus | creditAmount | updatedCredit |
            | 10           | 50           | 60            |
            | 0            | 10           | 10            |
            | 1000         | 0            | 1000          |