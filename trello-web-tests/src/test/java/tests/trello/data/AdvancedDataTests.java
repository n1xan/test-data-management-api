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
 * Advanced integration tests demonstrating complex scenarios
 * using trello-data module with automatic cleanup via TestDataCleanupPlugin.
 */
@ExecutionBrowser(browser = Browser.CHROME, lifecycle = Lifecycle.RESTART_EVERY_TIME)
public class AdvancedDataTests extends BaseTrelloTest {

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
    public void boardWithMultipleListsAndCards_when_createdViaAPI() {
        // Create a comprehensive board structure via API using factory
        var board = boardFactory.buildDefault();
        board.setName("Sprint Board " + System.currentTimeMillis());
        board.create();

        // Create sprint lists using factory with board dependency
        var sprintBacklog = listFactory
                .buildWithName(board.getId(), "Sprint Backlog")
                .create();

        var inProgress = listFactory.buildDefault(board.getId());
        inProgress.setName("In Progress");
        inProgress.create();

        var codeReview = listFactory.buildDefault(board.getId());
        codeReview.setName("Code Review");
        codeReview.create();

        var testing = listFactory.buildDefault(board.getId());
        testing.setName("Testing");
        testing.create();

        var done = listFactory.buildDefault(board.getId());
        done.setName("Done");
        done.create();

        // Create multiple cards in different lists using factory with list dependencies
        var userStory1 = cardFactory.buildDefault(sprintBacklog.getId());
        userStory1.setName("User Story: Login Feature");
        userStory1.create();

        var userStory2 = cardFactory.buildDefault(sprintBacklog.getId());
        userStory2.setName("User Story: Dashboard");
        userStory2.create();

        var bugFix = cardFactory.buildDefault(inProgress.getId());
        bugFix.setName("Bug Fix: Memory Leak");
        bugFix.create();

        var feature = cardFactory.buildDefault(codeReview.getId());
        feature.setName("Feature: Export Data");
        feature.create();

        var completedTask = cardFactory.buildDefault(done.getId());
        completedTask.setName("Task: Update Documentation");
        completedTask.create();

        // Verify the board structure in UI
        boardsPage.open();
        boardsPage.openBoardByTitle(board.getName());
        boardPage.assertNavigated();

        var listTitles = boardPage.getAllListsTitles();
        Assertions.assertEquals(5, listTitles.size(), "Should have 5 lists");
        Assertions.assertTrue(listTitles.contains("Sprint Backlog"), "Sprint Backlog should be present");
        Assertions.assertTrue(listTitles.contains("In Progress"), "In Progress should be present");
        Assertions.assertTrue(listTitles.contains("Code Review"), "Code Review should be present");
        Assertions.assertTrue(listTitles.contains("Testing"), "Testing should be present");
        Assertions.assertTrue(listTitles.contains("Done"), "Done should be present");

        // All entities will be automatically cleaned up by TestDataCleanupPlugin
    }

    @Test
    public void multipleBoardsWithDifferentStructures_when_createdViaAPI() {
        // Create different types of boards via API using factory
        
        // Project Management Board
        var projectBoard = boardFactory.buildDefault();
        projectBoard.setName("Project Management " + System.currentTimeMillis());
        projectBoard.create();

        var planningList = listFactory.buildDefault(projectBoard.getId());
        planningList.setName("Planning");
        planningList.create();

        var executionList = listFactory.buildDefault(projectBoard.getId());
        executionList.setName("Execution");
        executionList.create();

        // Bug Tracking Board
        var bugBoard = boardFactory.buildDefault();
        bugBoard.setName("Bug Tracking " + System.currentTimeMillis());
        bugBoard.create();

        var newBugsList = listFactory.buildDefault(bugBoard.getId());
        newBugsList.setName("New Bugs");
        newBugsList.create();

        var inProgressBugsList = listFactory.buildDefault(bugBoard.getId());
        inProgressBugsList.setName("In Progress");
        inProgressBugsList.create();

        var resolvedBugsList = listFactory.buildDefault(bugBoard.getId());
        resolvedBugsList.setName("Resolved");
        resolvedBugsList.create();

        // Create some cards using factory with list dependencies
        var projectCard = cardFactory.buildDefault(planningList.getId());
        projectCard.setName("Project Kickoff");
        projectCard.create();

        var bugCard = cardFactory.buildDefault(newBugsList.getId());
        bugCard.setName("Critical Bug: Login Issue");
        bugCard.create();

        // Verify both boards exist in UI
        boardsPage.open();
        boardsPage.assertNavigated();
        
        var boardAnchors = boardsPage.getAllBoardAnchors();
        var boardTitles = boardAnchors.stream()
                .map(anchor -> anchor.getAttribute("title"))
                .toList();
        
        Assertions.assertTrue(boardTitles.contains(projectBoard.getName()), "Project board should be visible");
        Assertions.assertTrue(boardTitles.contains(bugBoard.getName()), "Bug board should be visible");

        // TestDataCleanupPlugin will clean up all entities from both boards
    }

    @Test
    public void boardWithNestedStructure_when_createdViaAPI() {
        // Create a board with a complex nested structure using factory
        var board = boardFactory.buildDefault();
        board.setName("Complex Structure Board " + System.currentTimeMillis());
        board.create();

        // Create main workflow lists using factory with board dependency
        var todoList = listFactory.buildDefault(board.getId());
        todoList.setName("To Do");
        todoList.setPos(1.0);
        todoList.create();

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

        // Create multiple cards in each list using factory with list dependencies
        for (int i = 1; i <= 3; i++) {
            var todoCard = cardFactory.buildDefault(todoList.getId());
            todoCard.setName("Todo Task " + i);
            todoCard.create();

            var progressCard = cardFactory.buildDefault(inProgressList.getId());
            progressCard.setName("Progress Task " + i);
            progressCard.create();

            var reviewCard = cardFactory.buildDefault(reviewList.getId());
            reviewCard.setName("Review Task " + i);
            reviewCard.create();

            var doneCard = cardFactory.buildDefault(doneList.getId());
            doneCard.setName("Done Task " + i);
            doneCard.create();
        }

        // Verify the board structure
        boardsPage.open();
        boardsPage.openBoardByTitle(board.getName());
        boardPage.assertNavigated();

        var listTitles = boardPage.getAllListsTitles();
        Assertions.assertEquals(4, listTitles.size(), "Should have 4 lists");
        Assertions.assertTrue(listTitles.contains("To Do"), "To Do list should be present");
        Assertions.assertTrue(listTitles.contains("In Progress"), "In Progress list should be present");
        Assertions.assertTrue(listTitles.contains("Review"), "Review list should be present");
        Assertions.assertTrue(listTitles.contains("Done"), "Done list should be present");

        // All 1 board + 4 lists + 12 cards = 17 entities will be automatically cleaned up
    }
}
