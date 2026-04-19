package pages;

import factory.WebDriverFactory;
import utils.Waiters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class BasePage {
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final int DEFAULT_TIMEOUT = 10;
    protected String relativeUrl;
    public BasePage() {
        this.driver = WebDriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        PageFactory.initElements(driver, this);
        logger.debug("Инициализирована страница: {}", this.getClass().getSimpleName());
    }

    /**
     * Открывает текущую страницу по relativeUrl.
     */
    public BasePage open() {
        String fullUrl = getFullUrl();
        logger.info("Открытие страницы: {}", fullUrl);

        if (fullUrl.endsWith("null")) {
            logger.error("relativeUrl не инициализирован, класс: {}", this.getClass().getSimpleName());
            throw new IllegalStateException("relativeUrl is not initialized in " + this.getClass().getSimpleName());
        }

        driver.get(fullUrl);
        waitForPageLoad();
        return this;
    }

    /**
     * Возвращает полный URL страницы.
     * @return полный URL
     */
    public String getFullUrl() {
        return getBaseUrl() + relativeUrl;
    }

    /**
     * Возвращает базовый URL приложения из конфигурации.
     * @return базовый URL
     */
    protected String getBaseUrl() {
        return "https://wishlist.otus.kartushin.su";
    }

    /**
     * Проверяет, что страница загружена (переопределяется в наследниках).
     * @return true если страница загружена
     */
    public boolean isLoaded() {
        logger.debug("Проверка загрузки страницы: {}", this.getClass().getSimpleName());
        try {
            wait.until(ExpectedConditions.urlContains(relativeUrl != null ? relativeUrl : ""));
            return true;
        } catch (Exception e) {
            logger.warn("Страница не загрузилась: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Ждёт видимости элемента.
     * @param locator локатор элемента
     * @param timeout таймаут ожидания
     * @return видимый WebElement
     */
    protected WebElement waitForElementVisible(By locator, int timeout) {
        logger.debug("Ожидание видимости элемента ({} сек): {}", timeout, locator);
        return new WebDriverWait(driver, Duration.ofSeconds(timeout))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Кликает по WebElement с логированием.
     */
    protected BasePage click(WebElement element) {
        logger.info("Клик по элементу: {}", element.toString());
        element.click();
        return this;
    }

    /**
     * Вводит текст в WebElement.
     */
    protected BasePage sendKeys(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
        return this;
    }

    /**
     * Получает текст WebElement.
     */
    protected String getText(WebElement element) {
        return element.getText();
    }


    /**
     * Проверяет, что элемент отображается.
     * @param locator локатор элемента
     * @return true если элемент видим
     */
    public boolean isElementDisplayed(By locator) {
        try {
            return waitForElementVisible(locator, 3).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Ждёт загрузки страницы.
     */
    protected void waitForPageLoad() {
        logger.debug("Ожидание загрузки страницы");
        Waiters.waitForPageLoad(driver, DEFAULT_TIMEOUT);
    }

    /**
     * Ждёт указанное время (для отладки).
     * @param seconds секунды ожидания
     */
    protected void waitSeconds(int seconds) {
        logger.debug("Пауза {} сек", seconds);
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Поток прерван во время ожидания");
        }
    }

    /**
     * Проверяет, что WebElement отображается.
     */
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element != null && element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
