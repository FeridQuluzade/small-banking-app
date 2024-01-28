package az.dev.smallbankingapp.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyCustomerRequest {

    @NotBlank
    private String code;

}
