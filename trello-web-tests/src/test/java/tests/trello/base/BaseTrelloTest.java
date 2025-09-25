package tests.trello.base;

import board.BoardRepositoryFactory;
import card.CardRepositoryFactory;
import com.trello.pages.board.BoardPage;
import com.trello.pages.boards.BoardsPage;
import com.trello.pages.login.LoginPage;
import list.ListRepositoryFactory;
import solutions.bellatrix.data.plugins.TestDataCleanupPlugin;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

public class BaseTrelloTest extends WebTest {

    protected LoginPage loginPage;
    protected BoardsPage boardsPage;
    protected BoardPage boardPage;
    protected BoardRepositoryFactory boardFactory;
    protected ListRepositoryFactory listFactory;
    protected CardRepositoryFactory cardFactory;

    @Override
    protected void configure() {
        super.configure();
        // Add TestDataCleanupPlugin to automatically clean up test data
        addPlugin(TestDataCleanupPlugin.class);
    }

    @Override
    protected void beforeEach() throws Exception {
        super.beforeEach();
        // Initialize factory instances (this will register repositories)
        boardFactory = new BoardRepositoryFactory();
        listFactory = new ListRepositoryFactory();
        cardFactory = new CardRepositoryFactory();
    }
}
