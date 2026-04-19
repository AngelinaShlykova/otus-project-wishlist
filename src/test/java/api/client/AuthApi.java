package api.client;

import api.model.LoginRequest;
import api.model.LoginResponse;
import config.ConfigLoader;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class AuthApi {
    private static final Logger logger = LogManager.getLogger(AuthApi.class);
    private final String baseUrl;
    private static final String AUTH_ENDPOINT = "/api/auth/login";

    public AuthApi() {
        this.baseUrl = ConfigLoader.getInstance().getApiUrl();
    }

    /**
     * Выполняет авторизацию пользователя.
     */
    public LoginResponse login(String username, String password) {
        logger.info("🔑 Авторизация: {}", username);

        LoginRequest request = new LoginRequest(username, password);

        Response response = given()
                .contentType("application/json")
                .accept("application/json")
                .body(request)
                .when()
                .post(baseUrl + AUTH_ENDPOINT);

        response.then().assertThat().statusCode(200);

        LoginResponse result = response.as(LoginResponse.class);
        logger.info("Авторизация успешна");
        return result;
    }
}