package com.trello.pages.login;

import com.trello.pages.base.BaseTrelloPage;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.PasswordInput;
import solutions.bellatrix.web.components.TextInput;

public class LoginPage extends BaseTrelloPage {
    @Override
    protected String getUrl() {
        return "https://id.atlassian.com/login";
    }
    protected TextInput getUsernameInput() {
        return app().create().byId(TextInput.class, "username-uid1");
    }

    protected PasswordInput getPasswordInput() {
        return app().create().byId(PasswordInput.class, "password").toBeVisible();
    }

    protected Button getLoginButton() {
        return app().create().byId(Button.class, "login-submit");
    }

    protected Button getContinueButton() {
        return app().create().byXPath(Button.class, "//button[@data-testid='login-submit-idf-testid']").toBeVisible();
    }

    public void fillUsername(String username){
        getUsernameInput().setText(username);
        browser().waitForAjax();
    }

    public void fillPassword(String password){
        getPasswordInput().setPassword(password);
    }

    public void clickContinue() {
        getContinueButton().click();
        browser().waitForAjax();
    }

    public void submitLoginForm(){
        getLoginButton().click();
        browser().waitForAjax();
    }

    public void loginWithCredentials(String username, String password){
        fillUsername(username);
        clickContinue();
        fillPassword(password);
        submitLoginForm();
        browser().waitForAjax();
        browser().waitUntilPageLoadsCompletely();

        app().navigate().waitForPartialUrl("https://home.atlassian.com/?utm_source=identity");
        app().navigate().to("https://trello.com/appSwitcherLogin?login_hint=%s".formatted(username));
        app().navigate().waitForPartialUrl("https://trello.com/");
        app().browser().waitUntilPageLoadsCompletely();
        app().browser().waitForAjax();
    }
}
