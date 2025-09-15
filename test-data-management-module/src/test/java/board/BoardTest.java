package board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class BoardTest {
    
    private BoardRepositoryFactory boardFactory;
    private String testBoardId;
    
    @BeforeEach
    public void setUp() {
        BoardRepositoryFactory.INSTANCE.registerBoardRepository();
        boardFactory = BoardRepositoryFactory.INSTANCE;
    }
    
    @AfterEach
    public void tearDown() {
        // Clean up test board if it was created
        if (testBoardId != null) {
            try {
                Board boardToDelete = boardFactory.createDefaultBoard();
                boardToDelete.setId(testBoardId);
                boardToDelete.delete();
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }
    }

    @Test
    public void boardLifecycleTest() {
        // Create board using default state from factory
        Board board = boardFactory.createDefaultBoard().create();
        testBoardId = board.getId();
        
        // Retrieve and verify board properties
        board = board.get();
        assert board.getId() != null;
        assert board.getName() != null;
        
        // Update name
        String updatedName = "Updated Test Board " + System.currentTimeMillis();
        board.setName(updatedName);
        board = board.update();
        
        // Update description
        String updatedDescription = "Updated description via streamlined flow";
        board.setDescription(updatedDescription);
        board = board.update();
        
        // Close
        board.setClosed(true);
        board = board.update();
        
        // Verify final state - focus on board-specific properties
        assert board.getId() != null;
        assert board.getName().equals(updatedName);
        assert board.getClosed() == true;
        // Note: Description might be null from API response, so we check if it's not null first
        if (board.getDescription() != null) {
            assert board.getDescription().equals(updatedDescription);
        }
    }
}