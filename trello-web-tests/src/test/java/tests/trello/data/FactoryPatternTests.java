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
 * Tests demonstrating advanced factory patterns and model-centric approaches
 * for creating test data with automatic cleanup via TestDataCleanupPlugin.
 */
@ExecutionBrowser(browser = Browser.CHROME, lifecycle = Lifecycle.RESTART_EVERY_TIME)
public class FactoryPatternTests extends BaseTrelloTest {

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
    public void factoryWithDependencyResolution_when_creatingComplexStructure() {
        // Demonstrate using factory with dependency resolution
        var board = boardFactory.buildDefaultWithDependencies();
        board.setName("Dependency Resolution Board " + System.currentTimeMillis());
        board.create();

        // Create lists with factory methods that handle dependencies
        var backlogList = listFactory.buildDefault(board.getId());
        backlogList.setName("Backlog");
        backlogList.setPos(1.0);
        backlogList.create();

        var sprintList = listFactory.buildDefault(board.getId());
        sprintList.setName("Sprint");
        sprintList.setPos(2.0);
        sprintList.create();

        // Create cards using factory with automatic dependency handling
        var userStory = cardFactory.buildDefault(backlogList.getId());
        userStory.setName("User Story: Authentication");
        userStory.create();

        var task = cardFactory.buildDefault(sprintList.getId());
        task.setName("Task: Implement Login");
        task.create();

        // Verify structure in UI
        boardsPage.open();
        boardsPage.openBoardByTitle(board.getName());
        boardPage.assertNavigated();

        var listTitles = boardPage.getAllListsTitles();
        Assertions.assertTrue(listTitles.contains("Backlog"), "Backlog should be present");
        Assertions.assertTrue(listTitles.contains("Sprint"), "Sprint should be present");

        // All entities automatically cleaned up by TestDataCleanupPlugin
    }

    @Test
    public void factoryChaining_when_buildingComplexWorkflow() {
        // Demonstrate factory chaining for complex workflows
        var board = boardFactory.buildDefault();
        board.setName("Workflow Board " + System.currentTimeMillis());
        board.create();

        // Chain factory calls to create a complete workflow
        var todoList = listFactory.buildDefault(board.getId());
        todoList.setName("To Do");
        todoList.setPos(1.0);
        todoList.create();

        var inProgressList = listFactory.buildDefault(board.getId());
        inProgressList.setName("In Progress");
        inProgressList.setPos(2.0);
        inProgressList.create();

        var doneList = listFactory.buildDefault(board.getId());
        doneList.setName("Done");
        doneList.setPos(3.0);
        doneList.create();

        // Create workflow cards using factory chaining
        var epic = cardFactory.buildDefault(todoList.getId());
        epic.setName("Epic: User Management");
        epic.create();

        var story1 = cardFactory.buildDefault(todoList.getId());
        story1.setName("Story: User Registration");
        story1.create();

        var story2 = cardFactory.buildDefault(inProgressList.getId());
        story2.setName("Story: User Login");
        story2.create();

        var completedStory = cardFactory.buildDefault(doneList.getId());
        completedStory.setName("Story: Password Reset");
        completedStory.create();

        // Verify workflow structure
        boardsPage.open();
        boardsPage.openBoardByTitle(board.getName());
        boardPage.assertNavigated();

        var listTitles = boardPage.getAllListsTitles();
        Assertions.assertEquals(3, listTitles.size(), "Should have 3 workflow lists");
        Assertions.assertTrue(listTitles.contains("To Do"), "To Do should be present");
        Assertions.assertTrue(listTitles.contains("In Progress"), "In Progress should be present");
        Assertions.assertTrue(listTitles.contains("Done"), "Done should be present");

        // All workflow entities automatically cleaned up
    }

    @Test
    public void factoryWithCustomization_when_creatingSpecializedBoards() {
        // Demonstrate factory customization for specialized board types
        
        // Create a Kanban board
        var kanbanBoard = boardFactory.buildDefault();
        kanbanBoard.setName("Kanban Board " + System.currentTimeMillis());
        kanbanBoard.create();

        var kanbanTodo = listFactory.buildDefault(kanbanBoard.getId());
        kanbanTodo.setName("To Do");
        kanbanTodo.setPos(1.0);
        kanbanTodo.create();

        var kanbanDoing = listFactory.buildDefault(kanbanBoard.getId());
        kanbanDoing.setName("Doing");
        kanbanDoing.setPos(2.0);
        kanbanDoing.create();

        var kanbanDone = listFactory.buildDefault(kanbanBoard.getId());
        kanbanDone.setName("Done");
        kanbanDone.setPos(3.0);
        kanbanDone.create();

        // Create a Scrum board
        var scrumBoard = boardFactory.buildDefault();
        scrumBoard.setName("Scrum Board " + System.currentTimeMillis());
        scrumBoard.create();

        var scrumBacklog = listFactory.buildDefault(scrumBoard.getId());
        scrumBacklog.setName("Product Backlog");
        scrumBacklog.setPos(1.0);
        scrumBacklog.create();

        var scrumSprint = listFactory.buildDefault(scrumBoard.getId());
        scrumSprint.setName("Sprint Backlog");
        scrumSprint.setPos(2.0);
        scrumSprint.create();

        var scrumInProgress = listFactory.buildDefault(scrumBoard.getId());
        scrumInProgress.setName("In Progress");
        scrumInProgress.setPos(3.0);
        scrumInProgress.create();

        var scrumReview = listFactory.buildDefault(scrumBoard.getId());
        scrumReview.setName("Review");
        scrumReview.setPos(4.0);
        scrumReview.create();

        var scrumDone = listFactory.buildDefault(scrumBoard.getId());
        scrumDone.setName("Done");
        scrumDone.setPos(5.0);
        scrumDone.create();

        // Verify both boards exist
        boardsPage.open();
        boardsPage.assertNavigated();
        
        var boardAnchors = boardsPage.getAllBoardAnchors();
        var boardTitles = boardAnchors.stream()
                .map(anchor -> anchor.getAttribute("title"))
                .collect(Collectors.toList());
        
        Assertions.assertTrue(boardTitles.contains(kanbanBoard.getName()), "Kanban board should be visible");
        Assertions.assertTrue(boardTitles.contains(scrumBoard.getName()), "Scrum board should be visible");

        // All specialized board entities automatically cleaned up
    }

    @Test
    public void factoryWithDataVariations_when_testingDifferentScenarios() {
        // Demonstrate factory usage for different test data scenarios
        
        // Scenario 1: Empty board
        var emptyBoard = boardFactory.buildDefault();
        emptyBoard.setName("Empty Board " + System.currentTimeMillis());
        emptyBoard.create();

        // Scenario 2: Board with single list
        var singleListBoard = boardFactory.buildDefault();
        singleListBoard.setName("Single List Board " + System.currentTimeMillis());
        singleListBoard.create();

        var singleList = listFactory.buildDefault(singleListBoard.getId());
        singleList.setName("Only List");
        singleList.setPos(1.0);
        singleList.create();

        // Scenario 3: Board with multiple lists but no cards
        var multiListBoard = boardFactory.buildDefault();
        multiListBoard.setName("Multi List Board " + System.currentTimeMillis());
        multiListBoard.create();

        for (int i = 1; i <= 5; i++) {
            var list = listFactory.buildDefault(multiListBoard.getId());
            list.setName("List " + i);
            list.setPos((double) i);
            list.create();
        }

        // Scenario 4: Board with lists and cards
        var populatedBoard = boardFactory.buildDefault();
        populatedBoard.setName("Populated Board " + System.currentTimeMillis());
        populatedBoard.create();

        var populatedList = listFactory.buildDefault(populatedBoard.getId());
        populatedList.setName("Populated List");
        populatedList.setPos(1.0);
        populatedList.create();

        for (int i = 1; i <= 3; i++) {
            var card = cardFactory.buildDefault(populatedList.getId());
            card.setName("Card " + i);
            card.create();
        }

        // Verify all scenarios exist
        boardsPage.open();
        boardsPage.assertNavigated();
        
        var boardAnchors = boardsPage.getAllBoardAnchors();
        var boardTitles = boardAnchors.stream()
                .map(anchor -> anchor.getAttribute("title"))
                .collect(Collectors.toList());
        
        Assertions.assertTrue(boardTitles.contains(emptyBoard.getName()), "Empty board should be visible");
        Assertions.assertTrue(boardTitles.contains(singleListBoard.getName()), "Single list board should be visible");
        Assertions.assertTrue(boardTitles.contains(multiListBoard.getName()), "Multi list board should be visible");
        Assertions.assertTrue(boardTitles.contains(populatedBoard.getName()), "Populated board should be visible");

        // All test scenario entities automatically cleaned up
    }

    @Test
    public void factoryWithBuilderPattern_when_creatingComplexEntities() {
        // Demonstrate advanced builder pattern usage with factories
        
        // Create a complex board with detailed configuration
        var complexBoard = boardFactory.buildDefault();
        complexBoard.setName("Complex Configuration Board " + System.currentTimeMillis());
        complexBoard.create();

        // Create lists with specific configurations
        var priorityList = listFactory.buildDefault(complexBoard.getId());
        priorityList.setName("High Priority");
        priorityList.setPos(1.0);
        priorityList.create();

        var normalList = listFactory.buildDefault(complexBoard.getId());
        normalList.setName("Normal Priority");
        normalList.setPos(2.0);
        normalList.create();

        var lowPriorityList = listFactory.buildDefault(complexBoard.getId());
        lowPriorityList.setName("Low Priority");
        lowPriorityList.setPos(3.0);
        lowPriorityList.create();

        // Create cards with specific configurations
        var urgentCard = cardFactory.buildDefault(priorityList.getId());
        urgentCard.setName("URGENT: Critical Bug Fix");
        urgentCard.create();

        var normalCard = cardFactory.buildDefault(normalList.getId());
        normalCard.setName("Normal: Feature Request");
        normalCard.create();

        var lowPriorityCard = cardFactory.buildDefault(lowPriorityList.getId());
        lowPriorityCard.setName("Low: Documentation Update");
        lowPriorityCard.create();

        // Verify complex structure
        boardsPage.open();
        boardsPage.openBoardByTitle(complexBoard.getName());
        boardPage.assertNavigated();

        var listTitles = boardPage.getAllListsTitles();
        Assertions.assertEquals(3, listTitles.size(), "Should have 3 priority lists");
        Assertions.assertTrue(listTitles.contains("High Priority"), "High Priority should be present");
        Assertions.assertTrue(listTitles.contains("Normal Priority"), "Normal Priority should be present");
        Assertions.assertTrue(listTitles.contains("Low Priority"), "Low Priority should be present");

        // All complex entities automatically cleaned up
    }
}
