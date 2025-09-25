package tests.trello.login;

import com.trello.pages.boards.BoardsPage;
import com.trello.pages.login.LoginPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.web.infrastructure.Browser;
import solutions.bellatrix.web.infrastructure.ExecutionBrowser;
import solutions.bellatrix.web.infrastructure.Lifecycle;
import tests.trello.base.BaseTrelloTest;

@ExecutionBrowser(browser = Browser.CHROME, lifecycle = Lifecycle.RESTART_EVERY_TIME)
public class LoginTests extends BaseTrelloTest {

    @Override
    protected void beforeEach() throws Exception {
        super.beforeEach();
        loginPage = app().createPage(LoginPage.class);
        boardsPage = app().createPage(BoardsPage.class);
        loginPage.open();
    }

    @Test
    public void userAuthenticated_when_validCredentials() {
        loginPage.loginWithCredentials("n1xan@yahoo.com", "ОБИЧАМ-българия");
        boardsPage.assertNavigated();
    }
}

