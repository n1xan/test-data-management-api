package com.trello.pages.boards;

import com.trello.pages.base.BaseTrelloPage;
import org.openqa.selenium.Keys;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.TextInput;

import java.util.List;

public class BoardsPage extends BaseTrelloPage {
    @Override
    protected String getUrl() {
        return super.getUrl() + "/boards";
    }

    public List<Anchor> getAllBoardAnchors(){
        return app().create().allByXPath(Anchor.class, "//h3[text()='YOUR WORKSPACES']/../div/div/div/a[@title]");
    }

    public void openBoardByTitle(String title) {
        app().browser().waitUntil((x) -> !getAllBoardAnchors().isEmpty());
        var board = getAllBoardAnchors().stream().filter(b -> b.getAttribute("title").equals(title)).findFirst().orElseThrow();
        board.click();
        browser().waitForAjax();
    }

    public void createBoard(String title) {
        app().browser().waitUntil((x) -> !getAllBoardAnchors().isEmpty());
        getCreateBoardButton().click();
        getCreateBoardTitleInput().setText(title + Keys.ENTER);
        browser().waitForAjax();
    }

    private Button getCreateBoardButton() {
        return app().create().byXPath(Button.class, "//button[@data-testid='create-board-tile']");
    }

    private TextInput getCreateBoardTitleInput() {
        return app().create().byXPath(TextInput.class, "//input[@data-testid='create-board-title-input']").toBeVisible();
    }
}
