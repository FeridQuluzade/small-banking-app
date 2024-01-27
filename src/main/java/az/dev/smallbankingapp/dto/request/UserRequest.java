package az.dev.smallbankingapp.dto.request;

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

    @NotBlank
    private String gsmNumber;

}
