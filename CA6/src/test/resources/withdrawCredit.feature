Feature: Credit Withdrawing
    Scenario Outline: Withdrawing insufficient credit
        Given a user whose  account's credit status is saved in "<creditStatus>"
        When the user withdraw Insufficient "<withdrawCreditAmount>" from his account's credit
        Then the InsufficientCredit exception massage "<exceptionMassage>" should be raised
        And user account credit should not be changed

        Examples:
            | creditStatus | withdrawCreditAmount | exceptionMassage        |
            | 10           | 2000                 | Credit is insufficient. |
            | 0            | 1                    | Credit is insufficient. |
            | 1000         | 1000.25              | Credit is insufficient. |

    Scenario Outline: Withdrawing sufficient credit
        Given a user whose  account's credit status is saved in "<creditStatus>"
        When the user withdraw Sufficient "<withdrawCreditAmount>" from his account's credit
        Then user account credit "<updatedCredit>" should be changed with the credit amount without exception

        Examples:
            | creditStatus | withdrawCreditAmount | updatedCredit |
            | 50           | 10                   | 40            |
            | 40           | 15.5                 | 24.5          |
            | 1000         | 999                  | 1             |