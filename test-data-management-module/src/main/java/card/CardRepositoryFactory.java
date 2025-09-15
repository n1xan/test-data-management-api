package card;

import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.data.configuration.RepositoryFactory;
import solutions.bellatrix.data.contracts.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RepositoryFactory specifically for Card entities
 * Provides default state and repository management for Card operations
 */
public enum CardRepositoryFactory {
    INSTANCE;

    private final Map<Class<?>, Class<? extends Repository>> repositories = new ConcurrentHashMap<>();
    private CardRepository cardRepository;

    /**
     * Register the Card repository with the factory
     */
    public void registerCardRepository() {
        repositories.put(Card.class, CardRepository.class);
        // Also register with the main RepositoryFactory
        RepositoryFactory.INSTANCE.registerRepository(Card.class, CardRepository.class);
    }

    /**
     * Get the Card repository instance
     * @return CardRepository instance
     */
    public CardRepository getCardRepository() {
        if (cardRepository == null) {
            cardRepository = (CardRepository) SingletonFactory.getInstance(CardRepository.class);
        }
        return cardRepository;
    }

    /**
     * Create a Card with default test state
     * @return Card with default test values
     */
    public Card createDefaultCard() {
        return Card.builder()
                .name("Default Test Card " + System.currentTimeMillis())
                .description("This is a default test card created by BELLATRIX automation")
                .closed(false)
                .dueComplete(false)
                .build();
    }

    /**
     * Create a Card with default test state and a specific list ID
     * @param idList The ID of the list where the card should be created
     * @return Card with default test values and the specified list ID
     */
    public Card createDefaultCard(String idList) {
        return Card.builder()
                .name("Default Test Card " + System.currentTimeMillis())
                .description("This is a default test card created by BELLATRIX automation")
                .closed(false)
                .dueComplete(false)
                .idList(idList)
                .build();
    }
}