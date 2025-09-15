package board;

import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.data.configuration.RepositoryFactory;
import solutions.bellatrix.data.contracts.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RepositoryFactory specifically for Board entities
 * Provides default state and repository management for Board operations
 */
public enum BoardRepositoryFactory {
    INSTANCE;

    private final Map<Class<?>, Class<? extends Repository>> repositories = new ConcurrentHashMap<>();
    private BoardRepository boardRepository;

    /**
     * Register the Board repository with the factory
     */
    public void registerBoardRepository() {
        repositories.put(Board.class, BoardRepository.class);
        // Also register with the main RepositoryFactory
        RepositoryFactory.INSTANCE.registerRepository(Board.class, BoardRepository.class);
    }

    /**
     * Get the Board repository instance
     * @return BoardRepository instance
     */
    public BoardRepository getBoardRepository() {
        if (boardRepository == null) {
            boardRepository = (BoardRepository) SingletonFactory.getInstance(BoardRepository.class);
        }
        return boardRepository;
    }

    /**
     * Create a Board with default test state
     * @return Board with default test values
     */
    public Board createDefaultBoard() {
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
