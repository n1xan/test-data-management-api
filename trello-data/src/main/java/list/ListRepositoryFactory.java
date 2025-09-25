package list;

import solutions.bellatrix.data.http.contracts.EntityFactory;
import solutions.bellatrix.data.configuration.RepositoryFactory;

/**
 * Factory for List entities
 * Provides default state and repository management for List operations
 */
public class ListRepositoryFactory implements EntityFactory<List> {
    
    public ListRepositoryFactory() {
        // Register the List repository with the factory on instantiation
        RepositoryFactory.INSTANCE.registerRepository(List.class, ListRepository.class);
    }
    
    @Override
    public List buildDefault() {
        return List.builder()
                .name("Default Test List " + System.currentTimeMillis())
                .description("This is a default test list created by BELLATRIX automation")
                .closed(false)
                .subscribed(false)
                .build();
    }

    public List buildDefault(String idBoard) {
        return List.builder()
                .name("Default Test List " + System.currentTimeMillis())
                .description("This is a default test list created by BELLATRIX automation")
                .closed(false)
                .subscribed(false)
                .idBoard(idBoard)
                .build();
    }

    public List buildWithName(String idBoard, String name) {
        return List.builder()
                .name(name)
                .closed(false)
                .subscribed(false)
                .idBoard(idBoard)
                .build();
    }
}