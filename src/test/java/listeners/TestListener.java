package listeners;

import factory.WebDriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);

    private static final DateTimeFormatter FILE_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private static final String SCREENSHOTS_DIR = "screenshots";

    /**
     * @param context контекст выполнения тестов
     */
    @Override
    public void onStart(ITestContext context) {
        logger.info("ЗАПУСК ТЕСТОВ: {}", context.getName());
        logger.info("Время начала: {}", LocalDateTime.now().format(FILE_DATE_FORMAT));
        createScreenshotsDirectory();
    }

    /**
     * @param result результат выполнения теста
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = getFullTestName(result);
        logger.info("ТЕСТ УСПЕШЕН: {}", testName);
        logger.info("Время выполнения: {} мс",
                result.getEndMillis() - result.getStartMillis());
    }

    /**
     * @param result результат выполнения теста (с информацией об ошибке)
     */
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = getFullTestName(result);
        logger.error("ТЕСТ ПРОВАЛЕН: {}", testName);
        logger.error("Ошибка: {}", result.getThrowable() != null ?
                result.getThrowable().getMessage() : "Неизвестная ошибка");
        String screenshotPath = takeScreenshot(testName);
        if (screenshotPath != null) {
            logger.error("   📸 Скриншот сохранён: {}", screenshotPath);
        }

        if (result.getThrowable() != null) {
            logger.error("   Stack trace:", result.getThrowable());
        }
    }

    /**
     * @param result результат выполнения теста
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = getFullTestName(result);
        logger.warn("ТЕСТ ПРОПУЩЕН: {}", testName);
    }

    /**
     * @param result результат выполнения теста
     */
    @Override
    public void onTestStart(ITestResult result) {
        String testName = getFullTestName(result);
        logger.info("НАЧАЛО ТЕСТА: {}", testName);
        logger.info("Description: {}",
                result.getMethod().getDescription() != null ?
                        result.getMethod().getDescription() : "Нет описания");
    }

    /**
     * @param context контекст выполнения тестов
     */
    @Override
    public void onFinish(ITestContext context) {
        logger.info("ЗАВЕРШЕНИЕ ТЕСТОВ: {}", context.getName());
        logger.info("Результаты:");
        logger.info("Успешно: {}", context.getPassedTests().size());
        logger.info("Провалено: {}", context.getFailedTests().size());
        logger.info("Пропущено: {}", context.getSkippedTests().size());
        logger.info("Время завершения: {}",
                LocalDateTime.now().format(FILE_DATE_FORMAT));
    }

    private String takeScreenshot(String testName) {
        try {
            WebDriver driver = WebDriverFactory.getDriver();

            if (driver == null) {
                logger.warn(" WebDriver не инициализирован, скриншот не сделан");
                return null;
            }

            if (!(driver instanceof TakesScreenshot)) {
                logger.warn(" Драйвер не поддерживает скриншоты");
                return null;
            }
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);

            String timestamp = LocalDateTime.now().format(FILE_DATE_FORMAT);
            String safeTestName = testName.replaceAll("[^a-zA-Z0-9]", "_");
            String fileName = String.format("%s_%s.png", safeTestName, timestamp);

            Path destination = Paths.get(SCREENSHOTS_DIR, fileName);

            Files.createDirectories(destination.getParent());
            Files.copy(source.toPath(), destination);

            return destination.toAbsolutePath().toString();

        } catch (IOException e) {
            logger.error("Ошибка при создании скриншота: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при создании скриншота: {}", e.getMessage());
            return null;
        }
    }

    /**
     * @param result результат теста
     * @return строка с полным именем теста
     */
    private String getFullTestName(ITestResult result) {
        return String.format("%s.%s",
                result.getTestClass().getName(),
                result.getMethod().getMethodName());
    }

    private void createScreenshotsDirectory() {
        try {
            Path screenshotsPath = Paths.get(SCREENSHOTS_DIR);
            if (!Files.exists(screenshotsPath)) {
                Files.createDirectories(screenshotsPath);
                logger.info("Создана папка для скриншотов: {}",
                        screenshotsPath.toAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("Не удалось создать папку для скриншотов: {}", e.getMessage());
        }
    }

    /**
     * @param result результат выполнения теста
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Тест провален, но в пределах допустимого процента успеха: {}",
                getFullTestName(result));
    }
}
