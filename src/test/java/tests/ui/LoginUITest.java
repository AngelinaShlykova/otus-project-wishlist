package tests.ui;

import base.AbsBaseTest;
import config.ConfigLoader;
import pages.LoginPage;
import pages.WishlistPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import static utils.Waiters.waitSeconds;

public class LoginUITest extends AbsBaseTest {
    private static final Logger logger = LogManager.getLogger(LoginUITest.class);
    private String testLogin;
    private String testPassword;

    @org.testng.annotations.BeforeClass
    public void initCredentials() {
        ConfigLoader config = getConfig();
        testLogin = config.getTestUserLogin();
        testPassword = config.getTestUserPassword();
        logger.info("Credentials загружены: {}", maskLogin(testLogin));
    }

    /**
     * Тест успешной авторизации через UI.
     * @groups ui, login, smoke
     */
    @Test(description = "Успешная авторизация через UI",
            groups = {"ui", "login", "smoke"},
            priority = 1)
    public void successfulLoginUiTest() {
        logger.info("Тест: Успешная авторизация");
        LoginPage loginPage = new LoginPage();
        loginPage.open();
        Assert.assertTrue(loginPage.isLoaded(), "❌ Страница авторизации не загрузилась");
        logger.info("Страница авторизации загружена");
        WishlistPage wishlistPage = loginPage.loginSuccess(testLogin, testPassword);
        Assert.assertTrue(wishlistPage.isLoaded(),
                "Страница wishlist не загрузилась. URL: " + getDriver().getCurrentUrl());
        Assert.assertTrue(wishlistPage.isUserLoggedIn(),
                "Пользователь не авторизован");
        logger.info("Авторизация успешна");
    }

    /**
     * Тест авторизации с невалидным паролем.
     * @groups ui, login, negative
     */
    @Test(description = "Авторизация с невалидным паролем",
            groups = {"ui", "login", "negative"},
            priority = 2)
    public void loginWithInvalidPasswordUiTest() {
        logger.info("Тест: Невалидный пароль");

        LoginPage loginPage = new LoginPage();
        loginPage.open();
        loginPage.login(testLogin, "wrong_password");
        waitSeconds(2);
        Assert.assertTrue(loginPage.hasErrorMessage(),
                "Сообщение об ошибке не отображается");

        String errorMessage = loginPage.getErrorMessageText();
        Assert.assertTrue(errorMessage.contains("Неверное"),
                "Ожидалась ошибка 'Неверное имя пользователя или пароль'");

        logger.info("Ошибка отображена: {}", errorMessage);
    }

    /**
     * Тест авторизации с несуществующим пользователем.
     * @groups ui, login, negative
     */
    @Test(description = "Авторизация с несуществующим пользователем",
            groups = {"ui", "login", "negative"},
            priority = 3)
    public void loginWithNonExistentUserUiTest() {
        logger.info("Тест: Несуществующий пользователь");

        LoginPage loginPage = new LoginPage();
        loginPage.open();
        loginPage.login("nonexistent@test.com", "password123");
        waitSeconds(2);

        Assert.assertTrue(loginPage.hasErrorMessage(),
                "Сообщение об ошибке не отображается");

        String errorMessage = loginPage.getErrorMessageText();
        Assert.assertTrue(errorMessage.contains("Неверное"),
                "Ожидалась ошибка 'Неверное имя пользователя или пароль'");

        logger.info("Ошибка отображена: {}", errorMessage);
    }

    /**
     * Тест авторизации с пустыми полями.
     *
     * @groups ui, login, negative
     */
    @Test(description = "Авторизация с пустыми полями",
            groups = {"ui", "login", "negative"},
            priority = 4)
    public void loginWithEmptyFieldsUiTest() {
        logger.info("Тест: Пустые поля");

        LoginPage loginPage = new LoginPage();
        loginPage.open();
        loginPage.clickLoginButton();
        waitSeconds(2);

        Assert.assertTrue(loginPage.isLoaded(),
                "Не должна была перейти на другую страницу");

        logger.info("Форма не отправлена с пустыми полями");
    }

    /**
     * Тест проверки наличия элементов на странице авторизации.
     *
     * @groups ui, login
     */
    @Test(description = "Проверка элементов страницы авторизации",
            groups = {"ui", "login"},
            priority = 5)
    public void loginPageElementsUiTest() {
        logger.info("=== Тест: Элементы страницы ===");

        LoginPage loginPage = new LoginPage();
        loginPage.open();

        Assert.assertTrue(loginPage.isEmailInputDisplayed(),
                "Поле email не отображается");
        Assert.assertTrue(loginPage.isPasswordInputDisplayed(),
                "Поле пароля не отображается");
        Assert.assertTrue(loginPage.isLoginButtonClickable(),
                "Кнопка login не отображается");
        Assert.assertTrue(loginPage.hasRegisterLink(),
                "Ссылка на регистрацию не отображается");

        logger.info("Все элементы страницы отображаются");
    }

    private String maskLogin(String login) {
        if (login == null || login.length() < 5) {
            return "***";
        }
        int atIndex = login.indexOf('@');
        if (atIndex > 0) {
            return login.substring(0, 2) + "***" + login.substring(atIndex);
        }
        return login.substring(0, 2) + "***";
    }
}