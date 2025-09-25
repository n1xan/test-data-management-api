package com.trello.pages.board;

import com.trello.pages.base.BaseTrelloPage;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.components.Heading;

import java.util.List;

public class BoardPage extends BaseTrelloPage {
    @Override
    protected String getUrl() {
        return "https://trello.com/b/%s/sample-board".formatted(getBoardId());
    }

    private String getBoardId() {
        return "OCaY6Wn9";
    }

    public List<Heading> getAllLists(){
        return app().create().allByXPath(Heading.class, "//h2[@data-testid='list-name']");
    }

    public List<String> getAllListsTitles(){
        return getAllLists().stream().map(Heading::getText).toList();
    }
}
