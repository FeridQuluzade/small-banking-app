package az.dev.smallbankingapp.dto.request;

import az.dev.smallbankingapp.dto.request.marker.OnSent;
import az.dev.smallbankingapp.dto.request.marker.OnVerify;
import az.dev.smallbankingapp.error.validation.Patterns;
import az.dev.smallbankingapp.error.validation.PhoneNumber;
import az.dev.smallbankingapp.error.validation.Template;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpRequest {

    @PhoneNumber
    @NotBlank(groups = {OnSent.class, OnVerify.class})
    private String gsmNumber;

    @Template
    private String messageTemplate;

    @Pattern(regexp = Patterns.OTP)
    @NotBlank(groups = OnVerify.class)
    private String otpCode;

    public static OtpRequest withMessageTemplate(String gsmNumber, String messageTemplate) {
        var request = new OtpRequest();
        request.setGsmNumber(gsmNumber);
        request.setMessageTemplate(messageTemplate);
        return request;
    }

    public static OtpRequest withOtpCode(String gsmNumber, String otpCode) {
        var request = new OtpRequest();
        request.setGsmNumber(gsmNumber);
        request.setOtpCode(otpCode);
        return request;
    }

    @JsonIgnore
    public String getId() {
        return gsmNumber;
    }

}
