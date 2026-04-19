package factory.settings;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
public class ChromeDriverSettings implements IDriverSettings {

    private static final Logger logger = LogManager.getLogger(ChromeDriverSettings.class);

    @Override
    public WebDriver createDriver() {
        logger.info("Инициализация Chrome драйвера");

        String browserVersion = System.getProperty("browser.version", "116");
        logger.info("Версия Chrome: {}", browserVersion);

        try {
            WebDriverManager.chromedriver()
                    .browserVersion(browserVersion)
                    .setup();
        } catch (Exception e) {
            logger.warn("WebDriverManager failed for version {}, trying latest: {}",
                    browserVersion, e.getMessage());
            WebDriverManager.chromedriver().setup();
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        logger.info("Chrome драйвер успешно инициализирован");
        return driver;
    }
}
