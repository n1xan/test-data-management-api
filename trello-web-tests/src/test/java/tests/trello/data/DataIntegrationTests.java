package tests.trello.data;

import com.trello.pages.board.BoardPage;
import com.trello.pages.boards.BoardsPage;
import com.trello.pages.login.LoginPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.web.infrastructure.Browser;
import solutions.bellatrix.web.infrastructure.ExecutionBrowser;
import solutions.bellatrix.web.infrastructure.Lifecycle;
import tests.trello.base.BaseTrelloTest;

import java.util.stream.Collectors;

/**
 * Integration tests that demonstrate the use of trello-data module
 * for creating test data via API and then verifying it in the UI.
 * The TestDataCleanupPlugin automatically cleans up all created entities.
 */
@ExecutionBrowser(browser = Browser.CHROME, lifecycle = Lifecycle.RESTART_EVERY_TIME)
public class DataIntegrationTests extends BaseTrelloTest {

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
    public void boardCreatedViaAPI_when_verifiedInUI() {
        // Create board via API using factory - TestDataCleanupPlugin will track this
        var board = boardFactory.buildDefault().create();

        // Navigate to boards page and verify the board exists
        boardsPage.open();
        boardsPage.assertNavigated();
        
        // Verify board is visible in the UI
        var boardAnchors = boardsPage.getAllBoardAnchors();
        var boardTitles = boardAnchors.stream()
                .map(anchor -> anchor.getAttribute("title"))
                .collect(Collectors.toList());
        
        Assertions.assertTrue(boardTitles.contains(board.getName()), 
            "Board created via API should be visible in UI");
    }

    @Test
    public void boardWithListsAndCardsCreatedViaAPI_when_verifiedInUI() {
        // Create board via API using factory
        var board = boardFactory.buildDefault().create();

        // Create lists via API using factory with board dependency
        var todoList = listFactory.buildDefault(board.getId());
        todoList.setName("To Do");
        todoList.setPos(1.0);
        todoList.create();

        var doingList = listFactory.buildDefault(board.getId());
        doingList.setName("Doing");
        doingList.setPos(2.0);
        doingList.create();

        // Create cards via API using factory with list dependencies
        var card1 = cardFactory.buildDefault(todoList.getId());
        card1.setName("API Created Card 1");
        card1.create();

        var card2 = cardFactory.buildDefault(doingList.getId());
        card2.setName("API Created Card 2");
        card2.create();

        // Navigate to the board in UI
        boardsPage.open();
        boardsPage.openBoardByTitle(board.getName());
        boardPage.assertNavigated();

        // Verify lists are present
        var listTitles = boardPage.getAllListsTitles();
        Assertions.assertTrue(listTitles.contains("To Do"), "To Do list should be present");
        Assertions.assertTrue(listTitles.contains("Doing"), "Doing list should be present");

        // Note: Card verification would require additional page object methods
        // This demonstrates the integration between API data creation and UI verification
    }

    @Test
    public void multipleBoardsCreatedViaAPI_when_allCleanedUpAutomatically() {
        // Create multiple boards via API using factory - all will be tracked by TestDataCleanupPlugin
        var board1 = boardFactory.buildDefault();
        board1.setName("Cleanup Test Board 1 " + System.currentTimeMillis());
        board1.create();

        var board2 = boardFactory.buildDefault();
        board2.setName("Cleanup Test Board 2 " + System.currentTimeMillis());
        board2.create();

        var board3 = boardFactory.buildDefault();
        board3.setName("Cleanup Test Board 3 " + System.currentTimeMillis());
        board3.create();

        // Verify boards exist in UI
        boardsPage.open();
        boardsPage.assertNavigated();
        
        var boardAnchors = boardsPage.getAllBoardAnchors();
        var boardTitles = boardAnchors.stream()
                .map(anchor -> anchor.getAttribute("title"))
                .collect(Collectors.toList());
        
        Assertions.assertTrue(boardTitles.contains(board1.getName()), "Board 1 should be visible");
        Assertions.assertTrue(boardTitles.contains(board2.getName()), "Board 2 should be visible");
        Assertions.assertTrue(boardTitles.contains(board3.getName()), "Board 3 should be visible");

        // TestDataCleanupPlugin will automatically delete all three boards after test completion
    }

    @Test
    public void complexBoardStructureCreatedViaAPI_when_verifiedInUI() {
        // Create a complex board structure via API using factory
        var board = boardFactory.buildDefault();
        board.setName("Complex API Board " + System.currentTimeMillis());
        board.create();

        // Create multiple lists using factory with board dependency
        var backlogList = listFactory.buildDefault(board.getId());
        backlogList.setName("Backlog");
        backlogList.setPos(1.0);
        backlogList.create();

        var inProgressList = listFactory.buildDefault(board.getId());
        inProgressList.setName("In Progress");
        inProgressList.setPos(2.0);
        inProgressList.create();

        var reviewList = listFactory.buildDefault(board.getId());
        reviewList.setName("Review");
        reviewList.setPos(3.0);
        reviewList.create();

        var doneList = listFactory.buildDefault(board.getId());
        doneList.setName("Done");
        doneList.setPos(4.0);
        doneList.create();

        // Create cards in different lists using factory with list dependencies
        var backlogCard = cardFactory.buildDefault(backlogList.getId());
        backlogCard.setName("Backlog Item");
        backlogCard.create();

        var progressCard = cardFactory.buildDefault(inProgressList.getId());
        progressCard.setName("Work in Progress");
        progressCard.create();

        var reviewCard = cardFactory.buildDefault(reviewList.getId());
        reviewCard.setName("Under Review");
        reviewCard.create();

        // Navigate to the board and verify structure
        boardsPage.open();
        boardsPage.openBoardByTitle(board.getName());
        boardPage.assertNavigated();

        // Verify all lists are present
        var listTitles = boardPage.getAllListsTitles();
        Assertions.assertTrue(listTitles.contains("Backlog"), "Backlog list should be present");
        Assertions.assertTrue(listTitles.contains("In Progress"), "In Progress list should be present");
        Assertions.assertTrue(listTitles.contains("Review"), "Review list should be present");
        Assertions.assertTrue(listTitles.contains("Done"), "Done list should be present");

        // All entities (board, lists, cards) will be automatically cleaned up by TestDataCleanupPlugin
    }
}
