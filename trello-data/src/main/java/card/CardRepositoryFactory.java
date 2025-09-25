package card;

import solutions.bellatrix.data.http.contracts.EntityFactory;
import solutions.bellatrix.data.configuration.RepositoryFactory;

/**
 * Factory for Card entities
 * Provides default state and repository management for Card operations
 */
public class CardRepositoryFactory implements EntityFactory<Card> {
    
    public CardRepositoryFactory() {
        // Register the Card repository with the factory on instantiation
        RepositoryFactory.INSTANCE.registerRepository(Card.class, CardRepository.class);
    }
    
    @Override
    public Card buildDefault() {
        return Card.builder()
                .name("Default Test Card " + System.currentTimeMillis())
                .description("This is a default test card created by BELLATRIX automation")
                .closed(false)
                .dueComplete(false)
                .build();
    }
    
    public Card buildDefault(String idList) {
        var card = buildDefault();
        card.setIdList(idList);
        return card;
    }
}