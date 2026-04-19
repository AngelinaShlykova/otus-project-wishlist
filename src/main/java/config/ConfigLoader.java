package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public class ConfigLoader {

    private static final Logger logger = LogManager.getLogger(ConfigLoader.class);
    private static volatile ConfigLoader instance;

    private final Properties properties;

    /**
     * Приватный конструктор для предотвращения внешней инициализации.
     * Загружает конфигурацию при создании экземпляра.
     */
    private ConfigLoader() {
        logger.info("Инициализация ConfigLoader...");
        properties = new Properties();
        loadProperties();
        logger.info("ConfigLoader успешно инициализирован");
    }

    /**
     * Возвращает единственный экземпляр конфигурации (Singleton).
     *
     * @return экземпляр ConfigLoader
     */
    public static ConfigLoader getInstance() {
        if (instance == null) {
            synchronized (ConfigLoader.class) {
                if (instance == null) {
                    instance = new ConfigLoader();
                }
            }
        }
        return instance;
    }

    /**
     * Загружает свойства из основного и локального конфигов.
     */
    private void loadProperties() {
        try (InputStream baseStream = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (baseStream != null) {
                properties.load(baseStream);
                logger.info("Загружен базовый конфиг: config.properties");
            } else {
                logger.warn("Базовый конфиг config.properties не найден в resources");
            }
        } catch (IOException e) {
            logger.error("Ошибка чтения config.properties", e);
        }
        loadLocalProperties();
        logSafeConfig();
    }

    /**
     * Загружает локальные переопределения из config-local.properties.
     */
    private void loadLocalProperties() {
        String localConfigPath = "config-local.properties";

        try (InputStream localStream = new FileInputStream(localConfigPath)) {
            Properties localProps = new Properties();
            localProps.load(localStream);
            properties.putAll(localProps);
            logger.info("Загружены локальные переопределения: {}", localConfigPath);
        } catch (IOException e) {
            logger.debug("Локальный конфиг не найден или не читается (это нормально): {}",
                    localConfigPath);
        }
    }

    /**
     * Логирует настройки без чувствительных данных.
     */
    private void logSafeConfig() {
        logger.info("App URL: {}", properties.getProperty("app.url"));
        logger.info("API URL: {}", properties.getProperty("api.url"));
        logger.info("Browser: {} (headless: {})",
                properties.getProperty("browser.type"),
                properties.getProperty("browser.headless"));
    }

    public String getAppUrl() {
        return properties.getProperty("app.url");
    }

    public String getApiUrl() {
        return properties.getProperty("api.url");
    }

    public String getBrowserType() {
        return properties.getProperty("browser.type", "chrome");
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(
                properties.getProperty("browser.headless", "false")
        );
    }

    public int getTimeout() {
        return Integer.parseInt(
                properties.getProperty("browser.timeout", "10")
        );
    }

    public String getTestUserLogin() {
        return properties.getProperty("test.user.login");
    }

    public String getTestUserPassword() {
        return properties.getProperty("test.user.password");
    }
}
