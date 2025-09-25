package com.trello.pages.base;

import org.junit.jupiter.api.Assertions;
import solutions.bellatrix.web.pages.WebPage;

public class BaseTrelloPage extends WebPage {
    @Override
    protected String getUrl() {
        return "https://trello.com/u/%s".formatted(getUser());
    }

    private String getUser() {
        return "nikolay97110466";
    }

    public void assertNavigated() {
        Assertions.assertEquals(getUrl(), app().browser().getUrl(), "Page was not navigated");
    }
}
