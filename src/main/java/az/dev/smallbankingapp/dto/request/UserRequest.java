package az.dev.smallbankingapp.dto.request;

import az.dev.smallbankingapp.error.validation.PhoneNumber;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String password;

    @PhoneNumber
    @NotBlank
    private String gsmNumber;

}
