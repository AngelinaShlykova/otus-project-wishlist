package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RegistrationPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(RegistrationPage.class);

    @FindBy(css = "input[type='text'].form-control")
    private WebElement usernameInput;

    @FindBy(css = "input[type='email'].form-control")
    private WebElement emailInput;

    @FindBy(css = "input[type='password'].form-control")
    private WebElement passwordInput;

    @FindBy(css = "button[type='submit'].btn-primary")
    private WebElement registerButton;

    @FindBy(css = ".alert-danger, .text-danger")
    private WebElement errorMessage;

    @FindBy(css = ".alert-success")
    private WebElement successMessage;

    @FindBy(css = "h1, h2, .page-title")
    private WebElement pageTitle;

    @FindBy(linkText = "Войти")
    private WebElement loginLink;


    public RegistrationPage() {
        super();
        this.relativeUrl = "/register";
    }

    @Override
    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("/register"));
            wait.until(ExpectedConditions.visibilityOf(usernameInput));
            wait.until(ExpectedConditions.visibilityOf(emailInput));
            wait.until(ExpectedConditions.visibilityOf(passwordInput));
            logger.info("✅ Страница регистрации загружена");
            return true;
        } catch (Exception e) {
            logger.warn("Страница регистрации не загрузилась: {}", e.getMessage());
            return false;
        }
    }

    public RegistrationPage enterName(String name) {
        logger.info("Ввод имени пользователя: {}", name);
        sendKeys(usernameInput, name);
        return this;
    }

    public RegistrationPage enterEmail(String email) {
        logger.info("Ввод email: {}", maskEmail(email));
        sendKeys(emailInput, email);
        return this;
    }

    public RegistrationPage enterPassword(String password) {
        logger.info("Ввод пароля: ***");
        sendKeys(passwordInput, password);
        return this;
    }

    public RegistrationPage clickRegisterButton() {
        logger.info("Нажатие кнопки 'Зарегистрироваться'");
        click(registerButton);
        return this;
    }

    private String maskEmail(String email) {
        if (email == null || email.length() < 5) {
            return "***";
        }
        int atIndex = email.indexOf('@');
        if (atIndex > 0) {
            return email.substring(0, 2) + "***" + email.substring(atIndex);
        }
        return email.substring(0, 2) + "***";
    }

    /**
     * Проверяет наличие ссылки на вход.
     * @return true если ссылка отображается
     */
    public boolean hasLoginLink() {
        return isElementDisplayed(loginLink);
    }
}