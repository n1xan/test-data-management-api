package board;

import base.BaseTrelloRepository;

/**
 * Repository for Board entities
 * Extends BaseTrelloRepository with board-specific path parameter
 */
public class BoardRepository extends BaseTrelloRepository<Board> {
    
    public BoardRepository() {
        super(Board.class, "boards");
    }
}
