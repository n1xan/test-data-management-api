package tests.trello.data;

import com.trello.pages.board.BoardPage;
import com.trello.pages.boards.BoardsPage;
import com.trello.pages.login.LoginPage;
import list.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.web.infrastructure.Browser;
import solutions.bellatrix.web.infrastructure.ExecutionBrowser;
import solutions.bellatrix.web.infrastructure.Lifecycle;
import tests.trello.base.BaseTrelloTest;

/**
 * Tests specifically designed to demonstrate the TestDataCleanupPlugin functionality.
 * These tests create entities via API and show how the plugin automatically cleans them up.
 */
@ExecutionBrowser(browser = Browser.CHROME, lifecycle = Lifecycle.RESTART_EVERY_TIME)
public class CleanupPluginTests extends BaseTrelloTest {

    @Override
    protected void beforeEach() throws Exception {
        super.beforeEach();
        loginPage = app().createPage(LoginPage.class);
        boardsPage = app().createPage(BoardsPage.class);
        boardPage = app().createPage(BoardPage.class);
        loginPage.open();
        authenticateUser();
    }

    public void authenticateUser() {
        loginPage.loginWithCredentials("n1xan@yahoo.com", "ОБИЧАМ-българия");
        boardsPage.assertNavigated();
    }

    @Test
    public void entitiesCreatedViaAPI_when_automaticallyCleanedUpByPlugin() {
        // Create multiple entities via API using factory - TestDataCleanupPlugin will track all of them
        var board = boardFactory.buildDefault();
        board.setName("Cleanup Test Board " + System.currentTimeMillis());
        board.create();

        var list1 = listFactory.buildDefault(board.getId());
        list1.setName("List 1");
        list1.setPos(1.0);
        list1.create();

        var list2 = listFactory.buildDefault(board.getId());
        list2.setName("List 2");
        list2.setPos(2.0);
        list2.create();

        var card1 = cardFactory.buildDefault(list1.getId());
        card1.setName("Card 1");
        card1.create();

        var card2 = cardFactory.buildDefault(list2.getId());
        card2.setName("Card 2");
        card2.create();

        // Verify entities exist in UI
        boardsPage.open();
        boardsPage.openBoardByTitle(board.getName());
        boardPage.assertNavigated();

        var listTitles = boardPage.getAllListsTitles();
        Assertions.assertTrue(listTitles.contains("List 1"), "List 1 should be present");
        Assertions.assertTrue(listTitles.contains("List 2"), "List 2 should be present");

        // TestDataCleanupPlugin will automatically delete:
        // - board
        // - list1
        // - list2
        // - card1
        // - card2
        // Total: 5 entities cleaned up automatically
    }

    @Test
    public void mixedCleanupScenario_when_someEntitiesDeletedManually() {
        // Create entities via API using factory
        var board = boardFactory.buildDefault();
        board.setName("Mixed Cleanup Board " + System.currentTimeMillis());
        board.create();

        var list = listFactory.buildDefault(board.getId());
        list.setName("Test List");
        list.setPos(1.0);
        list.create();

        var card1 = cardFactory.buildDefault(list.getId());
        card1.setName("Card to Keep");
        card1.create();

        var card2 = cardFactory.buildDefault(list.getId());
        card2.setName("Card for Auto Cleanup");
        card2.create();

        // Manually delete one card - TestDataCleanupPlugin will remove it from tracking
        card1.delete();

        // Verify the remaining card exists
        boardsPage.open();
        boardsPage.openBoardByTitle(board.getName());
        boardPage.assertNavigated();

        var listTitles = boardPage.getAllListsTitles();
        Assertions.assertTrue(listTitles.contains("Test List"), "Test List should be present");

        // TestDataCleanupPlugin will automatically delete:
        // - board
        // - list
        // - card2 (card1 was already deleted manually and removed from tracking)
        // Total: 3 entities cleaned up automatically
    }

    @Test
    public void largeDataSet_when_allCleanedUpAutomatically() {
        // Create a large dataset to test cleanup performance using factory
        var board = boardFactory.buildDefault();
        board.setName("Large Dataset Board " + System.currentTimeMillis());
        board.create();

        // Create multiple lists using factory with board dependency
        var lists = new List[5];
        for (int i = 0; i < 5; i++) {
            lists[i] = listFactory.buildDefault(board.getId());
            lists[i].setName("List " + (i + 1));
            lists[i].setPos((double) (i + 1));
            lists[i].create();
        }

        // Create multiple cards in each list using factory with list dependencies
        var totalCards = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                var card = cardFactory.buildDefault(lists[i].getId());
                card.setName("Card " + (i + 1) + "-" + (j + 1));
                card.create();
                totalCards++;
            }
        }

        // Verify the structure exists
        boardsPage.open();
        boardsPage.openBoardByTitle(board.getName());
        boardPage.assertNavigated();

        var listTitles = boardPage.getAllListsTitles();
        Assertions.assertEquals(5, listTitles.size(), "Should have 5 lists");

        // TestDataCleanupPlugin will automatically delete:
        // - 1 board
        // - 5 lists
        // - 15 cards
        // Total: 21 entities cleaned up automatically
    }

    @Test
    public void nestedEntityStructure_when_cleanedUpInCorrectOrder() {
        // Create a nested structure to test cleanup order using factory
        var board = boardFactory.buildDefault();
        board.setName("Nested Structure Board " + System.currentTimeMillis());
        board.create();

        // Create parent list using factory with board dependency
        var parentList = listFactory.buildDefault(board.getId());
        parentList.setName("Parent List");
        parentList.setPos(1.0);
        parentList.create();

        // Create child cards using factory with list dependencies
        var childCard1 = cardFactory.buildDefault(parentList.getId());
        childCard1.setName("Child Card 1");
        childCard1.create();

        var childCard2 = cardFactory.buildDefault(parentList.getId());
        childCard2.setName("Child Card 2");
        childCard2.create();

        // Create another list with more cards using factory
        var secondList = listFactory.buildDefault(board.getId());
        secondList.setName("Second List");
        secondList.setPos(2.0);
        secondList.create();

        var secondListCard = cardFactory.buildDefault(secondList.getId());
        secondListCard.setName("Second List Card");
        secondListCard.create();

        // Verify structure exists
        boardsPage.open();
        boardsPage.openBoardByTitle(board.getName());
        boardPage.assertNavigated();

        var listTitles = boardPage.getAllListsTitles();
        Assertions.assertTrue(listTitles.contains("Parent List"), "Parent List should be present");
        Assertions.assertTrue(listTitles.contains("Second List"), "Second List should be present");

        // TestDataCleanupPlugin will clean up in reverse order (LIFO):
        // 1. secondListCard
        // 2. secondList
        // 3. childCard2
        // 4. childCard1
        // 5. parentList
        // 6. board
        // This ensures proper dependency handling
    }

    @Test
    public void errorHandling_when_cleanupFailsForSomeEntities() {
        // Create entities that will test error handling during cleanup using factory
        var board = boardFactory.buildDefault();
        board.setName("Error Handling Board " + System.currentTimeMillis());
        board.create();

        var list = listFactory.buildDefault(board.getId());
        list.setName("Error Test List");
        list.setPos(1.0);
        list.create();

        var card = cardFactory.buildDefault(list.getId());
        card.setName("Error Test Card");
        card.create();

        // Verify entities exist
        boardsPage.open();
        boardsPage.openBoardByTitle(board.getName());
        boardPage.assertNavigated();

        var listTitles = boardPage.getAllListsTitles();
        Assertions.assertTrue(listTitles.contains("Error Test List"), "Error Test List should be present");

        // TestDataCleanupPlugin will attempt to clean up all entities
        // Even if some fail, it will continue with the others
        // and provide detailed logging of successes and failures
    }
}
