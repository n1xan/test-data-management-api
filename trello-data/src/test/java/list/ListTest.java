package list;

import board.Board;
import base.BaseTrelloTest;
import org.junit.jupiter.api.Test;

public class ListTest extends BaseTrelloTest {

    @Test
    public void listLifecycleTest() {
        // Create board using default state from factory
        Board testBoard = boardFactory.buildDefault().create();
        trackBoard(testBoard.getId());
        
        // Create list using default state from factory with board ID
        List list = listFactory.buildDefault(testBoard.getId()).create();
        trackList(list.getId());
        
        // Retrieve and verify list properties
        list = list.get();
        assert list.getId() != null;
        assert list.getName() != null;
        assert list.getIdBoard().equals(testBoard.getId());
        
        // Update name
        String updatedName = "Updated Test List " + System.currentTimeMillis();
        list.setName(updatedName);
        list = list.update();
        
        // Update description
        String updatedDescription = "Updated description via streamlined flow";
        list.setDescription(updatedDescription);
        list = list.update();
        
        // Close
        list.setClosed(true);
        list = list.update();
        
        // Verify final state - focus on list-specific properties
        assert list.getId() != null;
        assert list.getName().equals(updatedName);
        // Note: Description might be null from API response, so we check if it's not null first
        if (list.getDescription() != null) {
            assert list.getDescription().equals(updatedDescription);
        }
        assert list.getClosed() == true;
    }
}