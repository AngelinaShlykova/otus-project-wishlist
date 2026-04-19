package tests.ui;

import base.AbsBaseTest;
import org.openqa.selenium.By;
import pages.LoginPage;
import pages.WishlistPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import static utils.Waiters.waitSeconds;

public class WishlistUITest extends AbsBaseTest {
    private static final Logger logger = LogManager.getLogger(WishlistUITest.class);

    private String testLogin;
    private String testPassword;

    @org.testng.annotations.BeforeClass
    public void initCredentials() {
        testLogin = getConfig().getTestUserLogin();
        testPassword = getConfig().getTestUserPassword();
    }

    /**
     * Тест создания wishlist через UI.
     * @groups ui, smoke, wishlist
     */
    @Test(description = "Создание wishlist через UI", groups = {"ui", "smoke", "wishlist"}, priority = 1)
    public void createWishlistUiTest() {
        logger.info("Тест: Создание wishlist через UI");

        LoginPage loginPage = new LoginPage();
        loginPage.open();
        WishlistPage wishlistPage = loginPage.loginSuccess(testLogin, testPassword);
        Assert.assertTrue(wishlistPage.isLoaded(), "Wishlist страница не загрузилась");
        logger.info("Авторизация успешна");

        String wishlistName = "Test Wishlist " + System.currentTimeMillis();

        wishlistPage.clickCreateWishlistButton();
        wishlistPage.enterWishlistName(wishlistName);
        wishlistPage.enterWishlistDescription("Test Description");
        wishlistPage.saveWishlist();

        waitSeconds(2);

        boolean wishlistCreated = wishlistPage.isElementDisplayed(
                By.xpath("//*[contains(text(), '" + wishlistName + "')]")
        );

        Assert.assertTrue(wishlistCreated, "Wishlist не создан: " + wishlistName);
        logger.info("Wishlist '{}' создан успешно", wishlistName);
    }

    /**
     * Тест добавления элемента в wishlist через UI.
     * @groups ui, wishlist
     */
    @Test(description = "Добавление элемента в wishlist через UI", groups = {"ui", "wishlist"}, priority = 2)
    public void addItemToWishlistUiTest() {
        logger.info("Тест: Добавление элемента в wishlist");

        LoginPage loginPage = new LoginPage();
        loginPage.open();
        WishlistPage wishlistPage = loginPage.loginSuccess(testLogin, testPassword);
        Assert.assertTrue(wishlistPage.isLoaded(), "Wishlist страница не загрузилась");

        wishlistPage.clickViewButton(0);
        waitSeconds(1);

        String itemName = "Test Item " + System.currentTimeMillis();
        wishlistPage.addItemWithAllFields(
                itemName,
                "Test Description",
                "https://example.com/product",
                "100"
        );

        waitSeconds(2);

        Assert.assertTrue(wishlistPage.hasItemByName(itemName), "Элемент не найден в wishlist");
        logger.info("Элемент добавлен: {}", itemName);
    }

    @Test(description = "Удаление wishlist через UI", groups = {"ui", "wishlist"}, priority = 3)
    public void deleteWishlistUiTest() {
        logger.info("Тест: Удаление wishlist через UI");

        LoginPage loginPage = new LoginPage();
        loginPage.open();
        WishlistPage wishlistPage = loginPage.loginSuccess(testLogin, testPassword);
        Assert.assertTrue(wishlistPage.isLoaded(), "Wishlist страница не загрузилась");

        String wishlistName = "Delete Test Wishlist " + System.currentTimeMillis();

        wishlistPage.clickCreateWishlistButton();
        wishlistPage.enterWishlistName(wishlistName);
        wishlistPage.enterWishlistDescription("Test Description");
        wishlistPage.saveWishlist();
        waitSeconds(2);

        Assert.assertTrue(wishlistPage.hasWishlistByName(wishlistName),
                "Wishlist '" + wishlistName + "' не создан");
        logger.info("Wishlist '{}' создан", wishlistName);

        boolean isDeleted = wishlistPage.deleteWishlistByName(wishlistName);
        waitSeconds(2);

        Assert.assertTrue(isDeleted, "Wishlist не был удалён");
        Assert.assertFalse(wishlistPage.hasWishlistByName(wishlistName),
                "Wishlist всё ещё отображается");

        logger.info("Wishlist '{}' успешно удалён", wishlistName);
    }

    /**
     * Тест выхода из системы.
     * @groups ui, smoke
     */
    @Test(description = "Выход из системы через UI", groups = {"ui", "smoke"}, priority = 4)
    public void logoutUiTest() {
        logger.info("=== Тест: Выход из системы ===");
        LoginPage loginPage = new LoginPage();
        loginPage.open();
        WishlistPage wishlistPage = loginPage.loginSuccess(testLogin, testPassword);
        Assert.assertTrue(wishlistPage.isUserLoggedIn(), "Пользователь не авторизован");
        logger.info("Пользователь авторизован");
        LoginPage loginPageAfterLogout = wishlistPage.logout();
        Assert.assertTrue(loginPageAfterLogout.isLoaded(), "Страница login не загрузилась после logout");
        logger.info("Выход выполнен успешно");
    }
}