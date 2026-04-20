package base;

import config.ConfigLoader;
import driver.BrowserType;
import factory.WebDriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import org.testng.ITestResult;

public abstract class AbsBaseTest {

    protected static final Logger logger = LogManager.getLogger(AbsBaseTest.class);
    protected ConfigLoader config;

    protected WebDriver getDriver() {
        return WebDriverFactory.getDriver();
    }

    protected ConfigLoader getConfig() {
        return config;
    }

    @BeforeClass
    public void beforeClass() {
        logger.info("Загрузка конфигурации проекта");
        config = ConfigLoader.getInstance();
        logger.info("Конфигурация успешно загружена");
    }

    @BeforeMethod
    public void setUp() {
        logger.info("Начало теста: {}",
                org.testng.Reporter.getCurrentTestResult().getMethod().getMethodName());

        BrowserType browser = BrowserType.valueOf(
                config.getBrowserType().toUpperCase()
        );

        logger.info("Запуск браузера: {}", browser);
        WebDriverFactory.createDriver(browser);

        getDriver().manage().window().maximize();
        getDriver().get(config.getAppUrl());

        logger.info("Браузер запущен, открыта страница: {}", config.getAppUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        if (result.isSuccess()) {
            logger.info("Тест '{}' успешно завершён", testName);
        } else if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("Тест '{}' упал: {}", testName,
                    result.getThrowable() != null ? result.getThrowable().getMessage() : "unknown error");
        } else if (result.getStatus() == ITestResult.SKIP) {
            logger.warn("Тест '{}' пропущен", testName);
        }

        logger.info("Закрытие драйвера...");
        WebDriverFactory.quitDriver();
        logger.info("Завершение теста: {}", testName);
    }

    @AfterClass
    public void afterClass() {
        logger.info("Завершение работы с конфигурацией");
        config = null;
    }
}
