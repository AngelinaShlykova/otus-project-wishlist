package api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("user")
    private UserInfo user;

    @Data
    @NoArgsConstructor
    public static class UserInfo {
        @JsonProperty("id")
        private Long id;
        @JsonProperty("email")
        private String email;
        @JsonProperty("name")
        private String name;
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
}