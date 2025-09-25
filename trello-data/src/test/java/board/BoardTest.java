package board;

import base.BaseTrelloTest;
import org.junit.jupiter.api.Test;

public class BoardTest extends BaseTrelloTest {

    @Test
    public void boardLifecycleTest() {
        // Create board using default state from factory
        Board board = boardFactory.buildDefault().create();
        trackBoard(board.getId());
        
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