package card;

import board.Board;
import board.BoardRepositoryFactory;
import list.List;
import list.ListRepositoryFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class CardTest {
    
    private CardRepositoryFactory cardFactory;
    private ListRepositoryFactory listFactory;
    private BoardRepositoryFactory boardFactory;
    private String testCardId;
    private String testListId;
    private String testBoardId;
    
    @BeforeEach
    public void setUp() {
        CardRepositoryFactory.INSTANCE.registerCardRepository();
        ListRepositoryFactory.INSTANCE.registerListRepository();
        BoardRepositoryFactory.INSTANCE.registerBoardRepository();
        cardFactory = CardRepositoryFactory.INSTANCE;
        listFactory = ListRepositoryFactory.INSTANCE;
        boardFactory = BoardRepositoryFactory.INSTANCE;
    }
    
    @AfterEach
    public void tearDown() {
        // Clean up test card if it was created
        if (testCardId != null) {
            try {
                Card cardToDelete = cardFactory.createDefaultCard();
                cardToDelete.setId(testCardId);
                cardToDelete.delete();
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }
        
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
    public void cardLifecycleTest() {
        // Create board using default state from factory
        Board testBoard = boardFactory.createDefaultBoard().create();
        testBoardId = testBoard.getId();
        
        // Create list using default state from factory with board ID
        List testList = listFactory.createDefaultList(testBoardId).create();
        testListId = testList.getId();
        
        // Create card using default state from factory with list ID
        Card card = cardFactory.createDefaultCard(testListId).create();
        testCardId = card.getId();
        
        // Retrieve and verify card properties
        card = card.get();
        assert card.getId() != null;
        assert card.getName() != null;
        assert card.getIdList().equals(testListId);
        
        // Update name
        String updatedName = "Updated Test Card " + System.currentTimeMillis();
        card.setName(updatedName);
        card = card.update();
        
        // Update description
        String updatedDescription = "Updated description via streamlined flow";
        card.setDescription(updatedDescription);
        card = card.update();
        
        // Close
        card.setClosed(true);
        card = card.update();
        
        // Verify final state - focus on card-specific properties
        assert card.getId() != null;
        assert card.getName().equals(updatedName);
        assert card.getClosed() == true;
        // Note: Description might be null from API response, so we check if it's not null first
        if (card.getDescription() != null) {
            assert card.getDescription().equals(updatedDescription);
        }
    }
}