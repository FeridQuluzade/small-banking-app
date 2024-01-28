package az.dev.smallbankingapp.dto.request;

import az.dev.smallbankingapp.error.validation.PhoneNumber;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @PhoneNumber
    @NotBlank
    private String gsmNumber;

    @NotBlank
    private String password;

}
