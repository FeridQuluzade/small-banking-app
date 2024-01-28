package az.dev.smallbankingapp.controller;

import az.dev.smallbankingapp.dto.request.OtpRequest;
import az.dev.smallbankingapp.dto.request.marker.OnSent;
import az.dev.smallbankingapp.dto.request.marker.OnVerify;
import az.dev.smallbankingapp.dto.response.OtpResponse;
import az.dev.smallbankingapp.service.OtpService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/otp")
public class OtpController {

    private final OtpService otpService;

    @Validated(OnSent.class)
    @PostMapping("/send")
    public OtpResponse send(@Valid @RequestBody OtpRequest sendOtpRequest) {
        return otpService.send(sendOtpRequest);
    }

    @Validated(OnVerify.class)
    @PostMapping("/verify")
    public void verify(@RequestBody @Valid OtpRequest verifyOtpRequest) {
        otpService.verify(verifyOtpRequest);
    }

}
