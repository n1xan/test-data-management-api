package list;

import base.BaseTrelloRepository;

/**
 * Repository for List entities
 * Extends BaseTrelloRepository with list-specific path parameter
 */
public class ListRepository extends BaseTrelloRepository<List> {
    
    public ListRepository() {
        super(List.class, "lists");
    }

    @Override
    public void delete(List entity) {
        entity.setClosed(true);
        super.update(entity);
    }
}
