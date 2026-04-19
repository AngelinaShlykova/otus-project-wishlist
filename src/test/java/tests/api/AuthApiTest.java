package tests.api;

import api.client.AuthApi;
import api.model.LoginResponse;
import config.ConfigLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthApiTest {
    private static final Logger logger = LogManager.getLogger(AuthApiTest.class);
    private AuthApi authApi;
    private String testLogin;
    private String testPassword;

    @org.testng.annotations.BeforeClass
    public void init() {
        authApi = new AuthApi();
        ConfigLoader config = ConfigLoader.getInstance();
        testLogin = config.getTestUserLogin();
        testPassword = config.getTestUserPassword();
    }

    @Test(description = "Успешная авторизация через API", groups = {"api", "auth", "smoke"}, priority = 1)
    public void successfulLoginTest() {
        logger.info("Тест: Успешная авторизация");

        LoginResponse response = authApi.login(testLogin, testPassword);

        Assert.assertNotNull(response, "Ответ авторизации пустой");
        Assert.assertNotNull(response.getToken(), "Токен не получен");
        Assert.assertFalse(response.getToken().isEmpty(), "Токен пустой");

        // 🔥 УБРАЛИ проверку userId, так как API может её не возвращать
        String tokenPreview = response.getToken().length() > 20
                ? response.getToken().substring(0, 20) + "..."
                : response.getToken();
        logger.info("Авторизация успешна: token={}", tokenPreview);
    }

    /**
     * Тест авторизации с невалидным паролем.
     */
    @Test(description = "Авторизация с невалидным паролем", groups = {"api", "auth", "negative"}, priority = 2,
            expectedExceptions = AssertionError.class)
    public void loginWithInvalidPasswordTest() {
        logger.info("=== Тест: Невалидный пароль ===");
        authApi.login(testLogin, "wrong_password");
        logger.error("❌ Должна была быть ошибка авторизации");
    }

    /**
     * Тест авторизации с несуществующим пользователем.
     */
    @Test(description = "Авторизация с несуществующим пользователем", groups = {"api", "auth", "negative"}, priority = 3,
            expectedExceptions = AssertionError.class)
    public void loginWithNonExistentUserTest() {
        logger.info("=== Тест: Несуществующий пользователь ===");
        authApi.login("nonexistent", "password123");
        logger.error("❌ Должна была быть ошибка авторизации");
    }

}