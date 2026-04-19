package exceptions;

public class BrowserTypeNotSupportedException extends RuntimeException {

    /**
     * Конструктор с сообщением об ошибке.
     *
     * @param message сообщение об ошибке
     */
    public BrowserTypeNotSupportedException(String message) {
        super(message);
    }

    /**
     * Конструктор с сообщением и причиной.
     *
     * @param message сообщение об ошибке
     * @param cause   причина исключения
     */
    public BrowserTypeNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
