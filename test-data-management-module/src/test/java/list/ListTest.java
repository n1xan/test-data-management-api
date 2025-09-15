package list;

import board.Board;
import board.BoardRepositoryFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class ListTest {
    
    private ListRepositoryFactory listFactory;
    private BoardRepositoryFactory boardFactory;
    private String testListId;
    private String testBoardId;
    
    @BeforeEach
    public void setUp() {
        ListRepositoryFactory.INSTANCE.registerListRepository();
        BoardRepositoryFactory.INSTANCE.registerBoardRepository();
        listFactory = ListRepositoryFactory.INSTANCE;
        boardFactory = BoardRepositoryFactory.INSTANCE;
    }
    
    @AfterEach
    public void tearDown() {
        // Clean up test list if it was created
        if (testListId != null) {
            try {
                List listToDelete = listFactory.createDefaultList();
                listToDelete.setId(testListId);
                listToDelete.delete();
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }
        
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
    public void listLifecycleTest() {
        // Create board using default state from factory
        Board testBoard = boardFactory.createDefaultBoard().create();
        testBoardId = testBoard.getId();
        
        // Create list using default state from factory with board ID
        List list = listFactory.createDefaultList(testBoardId).create();
        testListId = list.getId();
        
        // Retrieve and verify list properties
        list = list.get();
        assert list.getId() != null;
        assert list.getName() != null;
        assert list.getIdBoard().equals(testBoardId);
        
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
        assert list.getClosed() == true;
    }
}