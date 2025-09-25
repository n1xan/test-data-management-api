package card;

import base.BaseTrelloRepository;

/**
 * Repository for Card entities
 * Extends BaseTrelloRepository with card-specific path parameter
 */
public class CardRepository extends BaseTrelloRepository<Card> {
    
    public CardRepository() {
        super(Card.class, "cards");
    }
}
