package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(LoginPage.class);

    @FindBy(css = "input[type='text'].form-control")
    private WebElement emailInput;

    @FindBy(css = "input[type='password'].form-control")
    private WebElement passwordInput;

    @FindBy(css = "button[type='submit'].btn-primary")
    private WebElement loginButton;

    @FindBy(css = ".alert-danger, .text-danger")
    private WebElement errorMessage;

    @FindBy(linkText = "Регистрация")
    private WebElement registerLink;

    @FindBy(css = "h1, h2, .page-title")
    private WebElement pageTitle;

    public LoginPage() {
        super();
        this.relativeUrl = "/login";
    }

    @Override
    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("/login"));
            wait.until(ExpectedConditions.visibilityOf(emailInput));
            wait.until(ExpectedConditions.visibilityOf(passwordInput));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public LoginPage enterEmail(String email) {
        sendKeys(emailInput, email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        sendKeys(passwordInput, password);
        return this;
    }

    public LoginPage clickLoginButton() {
        click(loginButton);
        return this;
    }

    public LoginPage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        return this;
    }

    public WishlistPage loginSuccess(String email, String password) {
        login(email, password);
        waitSeconds(2);
        return new WishlistPage();
    }

    public boolean hasErrorMessage() {
        return isElementDisplayed(errorMessage);
    }

    public String getErrorMessageText() {
        if (hasErrorMessage()) {
            return getText(errorMessage);
        }
        return "";
    }

    public RegistrationPage goToRegistrationPage() {
        click(registerLink);
        return new RegistrationPage();
    }

    public boolean isEmailInputDisplayed() {
        return isElementDisplayed(emailInput);
    }

    public boolean isPasswordInputDisplayed() {
        return isElementDisplayed(passwordInput);
    }

    public boolean isLoginButtonClickable() {
        return isElementDisplayed(loginButton);
    }

    public boolean hasRegisterLink() {
        return isElementDisplayed(registerLink);
    }
}