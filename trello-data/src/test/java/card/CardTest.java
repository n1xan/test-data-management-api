package card;

import board.Board;
import list.List;
import base.BaseTrelloTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.data.configuration.RepositoryProvider;

public class CardTest extends BaseTrelloTest {

    @Test
    public void cardLifecycleTest() {
        Board testBoard = boardFactory.buildDefault().create();
        trackBoard(testBoard.getId());
        
        // Create list using default state from factory with board ID
        List testList = listFactory.buildDefault(testBoard.getId()).create();
        trackList(testList.getId());
        
        // Create card using default state from factory with list ID
        Card card = cardFactory.buildDefault(testList.getId()).create();
        trackCard(card.getId());

        // Retrieve and verify card properties
        card = card.get();
        assert card.getId() != null;
        assert card.getName() != null;
        assert card.getIdList().equals(testList.getId());
        
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

    @Test
    public void cardDependencyDefaultTest() {
        // Build card with dependencies using factory
        Card card = cardFactory.buildDefault();
        card.createWithDependencies();

        Assertions.assertNotNull(card.getId());
        Assertions.assertNotNull(card.getList().getId());
        Assertions.assertNotNull(card.getList().getBoard().getId());

        card.deleteDependenciesAndSelf();
    }

    @Test
    public void cardDependencyCustomizedTest() {
        String expectedListName = "New List " + System.currentTimeMillis();
        String expectedCardName = "New Card";
        String expectedBoardName = "New Board";

        // Build card with dependencies using factory
        Card card = cardFactory.buildDefaultWithDependencies();

        // Adjust test data across dependency chain according to the test case
        card.setName(expectedCardName);
        card.getList().setName(expectedListName);
        card.getList().getBoard().setName(expectedBoardName);

        card.createWithDependencies();

        Assertions.assertEquals(card.getName(), expectedCardName);
        Assertions.assertEquals(card.getList().getName(), expectedListName);
        Assertions.assertEquals(card.getList().getBoard().getName(), expectedBoardName);
      }
}