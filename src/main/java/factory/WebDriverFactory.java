package factory;

import driver.BrowserType;
import exceptions.BrowserTypeNotSupportedException;
import factory.settings.ChromeDriverSettings;
import factory.settings.IDriverSettings;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebDriverFactory {
    private static final Logger logger = LogManager.getLogger(WebDriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Создает и возвращает WebDriver для указанного типа браузера.
     *
     * @param browserType тип браузера
     * @return экземпляр WebDriver
     * @throws BrowserTypeNotSupportedException если тип браузера не поддерживается
     */
    public static WebDriver createDriver(BrowserType browserType) {
        logger.info("Создание драйвера для браузера: {}", browserType);

        IDriverSettings driverSettings;

        switch (browserType) {
            case CHROME:
                driverSettings = new ChromeDriverSettings();
                break;
            default:
                logger.error("Тип браузера не поддерживается: {}", browserType);
                throw new BrowserTypeNotSupportedException(
                        "Browser type not supported: " + browserType
                );
        }

        WebDriver driver = driverSettings.createDriver();
        driverThreadLocal.set(driver);
        return driver;
    }

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            logger.info("Закрытие драйвера");
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
