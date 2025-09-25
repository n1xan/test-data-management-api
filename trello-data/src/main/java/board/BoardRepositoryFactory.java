package board;

import solutions.bellatrix.data.configuration.RepositoryProvider;
import solutions.bellatrix.data.http.contracts.EntityFactory;
import solutions.bellatrix.data.configuration.FactoryProvider;

/**
 * Factory for Board entities
 * Provides default state and repository management for Board operations
 */
public class BoardRepositoryFactory implements EntityFactory<Board> {
    
    public BoardRepositoryFactory() {
    }
    
    @Override
    public Board buildDefault() {
        return Board.builder()
                .name("Default Test Board " + System.currentTimeMillis())
                .description("This is a default test board created by BELLATRIX automation")
                .closed(false)
                .pinned(false)
                .starred(false)
                .subscribed(false)
                .build();
    }
}