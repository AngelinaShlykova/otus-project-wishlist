package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;
public class WishlistPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(WishlistPage.class);

    @FindBy(css = "h1, h2, .page-title")
    private WebElement pageTitle;

    // Кнопка создания wishlist
    @FindBy(xpath = "//button[contains(text(), 'Создать новый список')]")
    private WebElement createWishlistButton;

    // Поля модального окна создания wishlist
    @FindBy(css = "input[placeholder='Название'], input[type='text'].form-control")
    private WebElement wishlistNameInput;

    @FindBy(css = "textarea[placeholder='Описание'], textarea.form-control")
    private WebElement wishlistDescriptionInput;

    @FindBy(css = "button[type='submit']")
    private WebElement saveWishlistButton;

    @FindBy(xpath = "//button[contains(text(), 'Добавить подарок')]")
    private WebElement addItemButton;

    @FindBy(xpath = "//div[@class='modal-content']//label[contains(text(), 'Название')]/following-sibling::input")
    private WebElement itemNameInput;

    @FindBy(xpath = "//div[@class='modal-content']//label[contains(text(), 'Описание')]/parent::div//textarea")
    private WebElement itemDescriptionInput;

    @FindBy(xpath = "//div[@class='modal-content']//input[@type='url']")
    private WebElement itemUrlInput;

    @FindBy(xpath = "//div[@class='modal-content']//input[@type='number']")
    private WebElement itemPriceInput;

    @FindBy(xpath = "//div[@class='modal-content']//input[@placeholder='https://example.com/image.jpg']")
    private WebElement itemImageInput;

    @FindBy(css = ".col, [class*='wishlist-item']")
    private List<WebElement> wishlistCards;

    @FindBy(xpath = "//button[contains(text(), 'Просмотр')]")
    private List<WebElement> viewButtons;

    @FindBy(xpath = "//button[contains(text(), 'Удалить')]")
    private List<WebElement> deleteButtons;

    @FindBy(xpath = "//a[contains(text(), 'Выйти')]")
    private WebElement logoutButton;

    @FindBy(css = ".alert-success, .alert-danger")
    private WebElement alertMessage;

    @FindBy(css = "button[type='submit'].btn-primary")
    private WebElement saveItemButton;


    public WishlistPage() {
        super();
        this.relativeUrl = "/wishlists";
    }


    @Override
    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("/wishlists"));
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            logger.info("Страница Wishlist загружена");
            return true;
        } catch (Exception e) {
            logger.warn("Страница Wishlist не загрузилась: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Проверяет, что пользователь авторизован.
     * @return true если пользователь авторизован
     */
    public boolean isUserLoggedIn() {
        try {
            boolean isLoggedIn = logoutButton.isDisplayed();
            logger.debug("Проверка авторизации (кнопка 'Выйти'): {}", isLoggedIn);
            return isLoggedIn;
        } catch (Exception e) {
            logger.warn("Кнопка 'Выйти' не найдена: {}", e.getMessage());
            return false;
        }
    }

    public WishlistPage clickCreateWishlistButton() {
        logger.info("Клик по кнопке 'Создать новый список'");
        click(createWishlistButton);
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class, 'card-title')]")
        ));
        return this;
    }

    public WishlistPage enterWishlistName(String name) {
        logger.info("Ввод названия wishlist: {}", name);
        sendKeys(wishlistNameInput, name);
        return this;
    }

    public WishlistPage enterWishlistDescription(String description) {
        logger.info("Ввод описания wishlist");
        sendKeys(wishlistDescriptionInput, description);
        return this;
    }

    public WishlistPage saveWishlist() {
        logger.info("Сохранение wishlist");
        click(saveWishlistButton);
        wait.until(ExpectedConditions.urlContains("/wishlists"));
        return this;
    }


    public WishlistPage clickAddItemButton() {
        logger.info("Клик по кнопке 'Добавить подарок'");
        click(addItemButton);
        wait.until(ExpectedConditions.urlContains("/wishlists"));
        return this;
    }

    public WishlistPage enterItemName(String itemName) {
        logger.info("Ввод названия подарка: {}", itemName);
        sendKeys(itemNameInput, itemName);
        return this;
    }

    public WishlistPage enterItemDescription(String description) {
        logger.info("Ввод описания подарка");
        sendKeys(itemDescriptionInput, description);
        return this;
    }

    public WishlistPage enterItemUrl(String url) {
        logger.info("Ввод ссылки на товар");
        sendKeys(itemUrlInput, url);
        return this;
    }

    public WishlistPage enterItemPrice(String price) {
        logger.info("Ввод цены: {}", price);
        sendKeys(itemPriceInput, price);
        return this;
    }


    public WishlistPage saveItem() {
        logger.info("Сохранение подарка");
        click(saveItemButton);
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class, 'card-title')]")
        ));
        return this;
    }

    /**
     * Добавляет элемент со всеми обязательными полями.
     */
    public WishlistPage addItemWithAllFields(String itemName, String description, String url, String price) {
        logger.info("Добавление элемента со всеми полями");
        clickAddItemButton();
        wait.until(ExpectedConditions.urlContains("/wishlists"));
        enterItemName(itemName);
        enterItemDescription(description);
        enterItemUrl(url);
        enterItemPrice(price);
        saveItem();
        return this;
    }

    public WishlistPage clickViewButton(int index) {
        logger.info("Клик по кнопке 'Просмотр' #{}", index);
        if (index < viewButtons.size()) {
            viewButtons.get(index).click();
            wait.until(ExpectedConditions.urlContains("/wishlists/"));
        }
        return this;
    }

    /**
     * Проверяет наличие wishlist по названию
     */
    public boolean hasWishlistByName(String wishlistName) {
        try {
            By locator = By.xpath("//div[contains(@class, 'card-title')][contains(text(), '" + wishlistName + "')]");
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator)) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Удаляет wishlist по названию
     */
    public boolean deleteWishlistByName(String wishlistName) {
        logger.info("Удаление wishlist: {}", wishlistName);
        try {
            By deleteButtonLocator = By.xpath(
                    "//div[contains(@class, 'card')][.//*[contains(text(), '" + wishlistName + "')]]//button[contains(text(), 'Удалить')]"
            );
            WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(deleteButtonLocator));
            deleteBtn.click();
            logger.info("Клик по кнопке 'Удалить' для wishlist '{}'", wishlistName);

            try {
                WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button[class*='danger'], button[class*='confirm'], .modal-footer .btn-danger")
                ));
                confirmBtn.click();
                logger.info("Подтверждение удаления в модальном окне");
            } catch (Exception e) {
                logger.info("Модальное окно не появилось (удаление мгновенное)");
            }
            wait.until(ExpectedConditions.urlContains("/wishlists"));
            boolean isDeleted = !hasWishlistByName(wishlistName);
            logger.info("Wishlist '{}' удалён: {}", wishlistName, isDeleted ? "✅" : "❌");
            return isDeleted;

        } catch (Exception e) {
            logger.error("Ошибка удаления wishlist '{}': {}", wishlistName, e.getMessage());
            return false;
        }
    }

    /**
     * Проверяет наличие элемента в wishlist по названию.
     */
    public boolean hasItemByName(String itemName) {
        try {
            By locator = By.xpath("//*[contains(text(), '" + itemName + "')]");
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator)) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public WishlistPage clickLogoutButton() {
        logger.info("Клик по кнопке 'Выйти'");
        click(logoutButton);
        return this;
    }

    public LoginPage logout() {
        logger.info("Выход из системы");
        clickLogoutButton();
        wait.until(ExpectedConditions.urlContains("/login"));
        return new LoginPage();
    }

}