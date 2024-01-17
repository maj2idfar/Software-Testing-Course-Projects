Feature: Item Removing
    Scenario: Removing only-one-in-buy-list commodity
        Given a user with only-one-in-buy-list commodity
        When the user remove the commodity from his buy-list
        Then the commodity should be deleted

    Scenario: Removing more-than-one-in-buy-list commodity
        Given a user with more-than-one-in-buy-list commodity
        When the user remove the commodity from his buy-list
        Then the commodity number should be updated

    Scenario Outline: Removing none-in-buy-list commodity
        Given a user with none-in-buy-list commodity
        When the user remove the commodity from his buy-list
        Then "<CommodityIsNotInBuyList>" exception message should be raised

        Examples:
            | CommodityIsNotInBuyList           |
            | Commodity is not in the buy list. |