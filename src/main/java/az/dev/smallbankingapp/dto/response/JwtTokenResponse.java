package az.dev.smallbankingapp.dto.response;

import lombok.Data;

@Data
public class JwtTokenResponse {

    private String accessToken;
    private boolean verified;
    private String tokenType = "Bearer";

    public JwtTokenResponse(String accessToken, boolean verified) {
        this.accessToken = accessToken;
        this.verified = verified;
    }

}
