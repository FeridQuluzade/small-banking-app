package az.dev.smallbankingapp.event.listener;

import az.dev.smallbankingapp.dto.request.OtpRequest;
import az.dev.smallbankingapp.event.NonVerifiedUserLoginEvent;
import az.dev.smallbankingapp.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationEventListener {

    private static final String DEFAULT_OTP_MESSAGE = "OTP: ";

    private final OtpService otpService;

    @Async
    @EventListener(NonVerifiedUserLoginEvent.class)
    public void onCreateEvent(NonVerifiedUserLoginEvent event) {
        otpService.send(OtpRequest.withMessageTemplate(event.getGsmNumber(), DEFAULT_OTP_MESSAGE));
    }

}
