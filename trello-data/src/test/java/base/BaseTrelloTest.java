package base;

import board.Board;
import board.BoardRepositoryFactory;
import card.Card;
import card.CardRepositoryFactory;
import list.List;
import list.ListRepositoryFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashSet;
import java.util.Set;

/**
 * Base test class for Trello API tests
 * Manages factory instances and provides common setup/teardown functionality
 */
public abstract class BaseTrelloTest {
    
    // Factory instances
    protected BoardRepositoryFactory boardFactory;
    protected ListRepositoryFactory listFactory;
    protected CardRepositoryFactory cardFactory;
    
    // Track created entities for cleanup
    protected Set<String> createdBoardIds = new HashSet<>();
    protected Set<String> createdListIds = new HashSet<>();
    protected Set<String> createdCardIds = new HashSet<>();
    
    @BeforeEach
    public void setUp() {
        // Initialize factory instances (this will register repositories)
        boardFactory = new BoardRepositoryFactory();
        listFactory = new ListRepositoryFactory();
        cardFactory = new CardRepositoryFactory();
    }
    
    @AfterEach
    public void tearDown() {
        // Clean up test data in reverse dependency order
        cleanupCards();
        cleanupLists();
        cleanupBoards();
    }

    /**
     * Clean up all created cards
     */
    protected void cleanupCards() {
        for (String cardId : createdCardIds) {
            try {
                Card cardToDelete = cardFactory.buildDefault();
                cardToDelete.setId(cardId);
                cardToDelete.delete();
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }
        createdCardIds.clear();
    }
    
    /**
     * Clean up all created lists
     */
    protected void cleanupLists() {
        for (String listId : createdListIds) {
            try {
                List listToDelete = listFactory.buildDefault();
                listToDelete.setId(listId);
                listToDelete.delete();
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }
        createdListIds.clear();
    }
    
    /**
     * Clean up all created boards
     */
    protected void cleanupBoards() {
        for (String boardId : createdBoardIds) {
            try {
                Board boardToDelete = boardFactory.buildDefault();
                boardToDelete.setId(boardId);
                boardToDelete.delete();
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }
        createdBoardIds.clear();
    }
    
    /**
     * Track a created board for cleanup
     */
    protected void trackBoard(String boardId) {
        if (boardId != null) {
            createdBoardIds.add(boardId);
        }
    }
    
    /**
     * Track a created list for cleanup
     */
    protected void trackList(String listId) {
        if (listId != null) {
            createdListIds.add(listId);
        }
    }
    
    /**
     * Track a created card for cleanup
     */
    protected void trackCard(String cardId) {
        if (cardId != null) {
            createdCardIds.add(cardId);
        }
    }
}
