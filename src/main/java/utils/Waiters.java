package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Waiters {
    private static final Logger logger = LogManager.getLogger(Waiters.class);

    public static boolean waitForPageLoad(WebDriver driver, int timeout) {
        logger.debug("Ожидание загрузки страницы ({} сек)...", timeout);

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));

            wait.until(webDriver ->
                    ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState")
                            .equals("complete")
            );
            waitSeconds(1);
            logger.debug("Страница загружена");
            return true;
        } catch (TimeoutException e) {
            logger.warn("Таймаут ожидания загрузки страницы");
            return false;
        } catch (Exception e) {
            logger.error("Ошибка при ожидании загрузки страницы: {}", e.getMessage());
            return false;
        }
    }

    public static void waitSeconds(int seconds) {
        logger.trace("Пауза {} сек", seconds);
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Поток прерван во время ожидания");
        }
    }

}
