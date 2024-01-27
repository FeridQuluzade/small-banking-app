package az.dev.smallbankingapp.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank
    private String gsmNumber;

    @NotBlank
    private String password;

}
