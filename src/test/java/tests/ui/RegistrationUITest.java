package tests.ui;

import base.AbsBaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.RegistrationPage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static utils.Waiters.waitSeconds;

public class RegistrationUITest extends AbsBaseTest {
    private static final Logger logger = LogManager.getLogger(RegistrationUITest.class);

    private String generatedEmail;
    private String generatedPassword;

    /**
     * Тест успешной регистрации.
     */
    @Test(description = "Успешная регистрация нового пользователя", groups = {"ui", "smoke", "registration"}, priority = 1)
    public void registerNewUserUiTest() {
        logger.info("Тест: Успешная регистрация");

        String timestamp = String.valueOf(System.currentTimeMillis());
        String name = "TestUser_" + timestamp;
        generatedEmail = "test_" + timestamp + "@test.com"; // Уникальный email
        generatedPassword = "Password123!";

        logger.info("Регистрация пользователя: {}, Email: {}", name, generatedEmail);

        LoginPage loginPage = new LoginPage();
        loginPage.open();
        RegistrationPage regPage = loginPage.goToRegistrationPage();
        Assert.assertTrue(regPage.isLoaded(), "Страница регистрации не загрузилась");

        regPage.enterName(name);
        regPage.enterEmail(generatedEmail);
        regPage.enterPassword(generatedPassword);
        regPage.clickRegisterButton();

        waitSeconds(3);
        String currentUrl = getDriver().getCurrentUrl();
        boolean success = currentUrl.contains("/wishlists") || currentUrl.contains("/login");

        Assert.assertTrue(success, "Регистрация не прошла успешно. Текущий URL: " + currentUrl);
        logger.info("Регистрация прошла успешно");

        saveCredentials(generatedEmail, generatedPassword);
    }

    /**
     * Проверка элементов на странице регистрации.
     */
    @Test(description = "Проверка наличия элементов на странице регистрации", groups = {"ui", "registration"}, priority = 2)
    public void registrationPageElementsUiTest() {
        logger.info("Тест: Элементы страницы регистрации");

        LoginPage loginPage = new LoginPage();
        loginPage.open();
        RegistrationPage regPage = loginPage.goToRegistrationPage();

        Assert.assertTrue(regPage.isLoaded(), "Страница не загружена");
        Assert.assertTrue(regPage.hasLoginLink(), "Ссылка 'Войти' не найдена");

        logger.info("Все элементы на месте");
    }

    /**
     * Сохраняет логин и пароль в test-credentials.properties
     */
    private void saveCredentials(String email, String password) {
        try {
            Properties props = new Properties();
            props.setProperty("test.user.login", email);
            props.setProperty("test.user.password", password);

            String filePath = "src/test/resources/test-credentials.properties";

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                props.store(fos, "Auto-generated credentials");
            }
            logger.info("Credentials сохранены в {}", filePath);
        } catch (IOException e) {
            logger.error("Ошибка сохранения credentials: {}", e.getMessage());
        }
    }
}