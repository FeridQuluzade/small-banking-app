package az.dev.smallbankingapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpResponse {

    private String uuid;
    private Long expiresIn;
    private Long resendAvailableIn;

}