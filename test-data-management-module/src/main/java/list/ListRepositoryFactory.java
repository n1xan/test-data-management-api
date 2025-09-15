package list;

import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.data.configuration.RepositoryFactory;
import solutions.bellatrix.data.contracts.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RepositoryFactory specifically for List entities
 * Provides default state and repository management for List operations
 */
public enum ListRepositoryFactory {
    INSTANCE;

    private final Map<Class<?>, Class<? extends Repository>> repositories = new ConcurrentHashMap<>();
    private ListRepository listRepository;

    /**
     * Register the List repository with the factory
     */
    public void registerListRepository() {
        repositories.put(List.class, ListRepository.class);
        // Also register with the main RepositoryFactory
        RepositoryFactory.INSTANCE.registerRepository(List.class, ListRepository.class);
    }

    /**
     * Get the List repository instance
     * @return ListRepository instance
     */
    public ListRepository getListRepository() {
        if (listRepository == null) {
            listRepository = (ListRepository) SingletonFactory.getInstance(ListRepository.class);
        }
        return listRepository;
    }

    /**
     * Create a List with default test state
     * @return List with default test values
     */
    public List createDefaultList() {
        return List.builder()
                .name("Default Test List " + System.currentTimeMillis())
                .description("This is a default test list created by BELLATRIX automation")
                .closed(false)
                .pos(1.0)
                .subscribed(false)
                .build();
    }

    /**
     * Create a List with default test state and a specific board ID
     * @param idBoard The ID of the board where the list should be created
     * @return List with default test values and the specified board ID
     */
    public List createDefaultList(String idBoard) {
        return List.builder()
                .name("Default Test List " + System.currentTimeMillis())
                .description("This is a default test list created by BELLATRIX automation")
                .closed(false)
                .pos(1.0)
                .subscribed(false)
                .idBoard(idBoard)
                .build();
    }
}
