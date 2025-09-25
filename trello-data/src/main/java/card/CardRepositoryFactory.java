package card;

import list.List;
import list.ListRepository;
import solutions.bellatrix.data.configuration.RepositoryProvider;
import solutions.bellatrix.data.http.contracts.EntityFactory;
import solutions.bellatrix.data.configuration.FactoryProvider;

/**
 * Factory for Card entities
 * Provides default state and repository management for Card operations
 */
public class CardRepositoryFactory implements EntityFactory<Card> {
    
    public CardRepositoryFactory() {
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