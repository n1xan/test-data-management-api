package tests.trello.boards;

import com.github.javafaker.Faker;
import com.trello.pages.board.BoardPage;
import com.trello.pages.boards.BoardsPage;
import com.trello.pages.login.LoginPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.infrastructure.Browser;
import solutions.bellatrix.web.infrastructure.ExecutionBrowser;
import solutions.bellatrix.web.infrastructure.Lifecycle;
import tests.trello.base.BaseTrelloTest;

import java.util.List;

@ExecutionBrowser(browser = Browser.CHROME, lifecycle = Lifecycle.RESTART_EVERY_TIME)
public class BoardsTests extends BaseTrelloTest {

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
    public void boardNavigated_when_clickFromWorkspace() {
        boardsPage.openBoardByTitle("Sample Board");
        boardPage.assertNavigated();
        var boardLists = boardPage.getAllListsTitles();
        Assertions.assertEquals(List.of("To Do", "Doing", "Done"), boardLists, "Default Lists not found");
    }

    @Test
    public void boardCreatedWithDefaultLists_when_createInWorkspace() {
        var existingCart = cardFactory.createWithDependencies();
        existingCart.setName("Test Card " + new Faker().number().digits(5));
        existingCart.create();


        boardsPage.openBoardByTitle(existingCart.getName());
        var boardLists = boardPage.getAllListsTitles();
        Assertions.assertNotEquals(List.of("To Do", "Doing", "Done"), boardLists, "Default Lists were found");
    }
}

