package tests.trello.base;

import board.Board;
import board.BoardRepository;
import board.BoardRepositoryFactory;
import card.Card;
import card.CardRepository;
import card.CardRepositoryFactory;
import com.trello.pages.board.BoardPage;
import com.trello.pages.boards.BoardsPage;
import com.trello.pages.login.LoginPage;
import list.List;
import list.ListRepository;
import list.ListRepositoryFactory;
import solutions.bellatrix.data.configuration.FactoryProvider;
import solutions.bellatrix.data.configuration.RepositoryProvider;
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

        registerRepositoriesAndFactories();
    }

    private static void registerRepositoriesAndFactories() {
        // Register the factories
        FactoryProvider.INSTANCE.register(Card.class, CardRepositoryFactory.class);
        FactoryProvider.INSTANCE.register(List.class, ListRepositoryFactory.class);
        FactoryProvider.INSTANCE.register(Board.class, BoardRepositoryFactory.class);

        // Register the repositories
        RepositoryProvider.INSTANCE.register(Card.class, CardRepository.class);
        RepositoryProvider.INSTANCE.register(List.class, ListRepository.class);
        RepositoryProvider.INSTANCE.register(Board.class, BoardRepository.class);
    }
}
