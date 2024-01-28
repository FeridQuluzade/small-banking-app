package az.dev.smallbankingapp.domain;

import az.dev.smallbankingapp.util.UuidProvider;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.Data;

@Data
public class Otp {

    private String phoneNumber;
    private String otpCode;
    private String uuid;
    private LocalDateTime sentTime;
    private Integer numberOfSending;
    private Integer attempts;

    public void addAttempt() {
        this.attempts++;
    }

    public void addNumberOfSending() {
        this.numberOfSending++;
    }

    public boolean isFirstTimeSending() {
        return this.numberOfSending == 0;
    }

    public boolean isValid(String code) {
        return this.otpCode.equals(code);
    }

    public void updateFrom(String otpCode) {
        this.setUuid(UuidProvider.generate());
        this.setOtpCode(otpCode);
        this.setSentTime(LocalDateTime.now());
        this.addNumberOfSending();
    }

    public boolean isTemporarilyBlocked(Integer maxNumberOfSending, Integer maxNumberOfVerifying) {
        return isMaxNumberOfSendingExceeded(maxNumberOfSending) ||
                isMaxAttemptsExceeded(maxNumberOfVerifying);
    }

    public boolean isTimeElapsed(Long timeToElapse) {
        return this.sentTime.until(LocalDateTime.now(), ChronoUnit.SECONDS) > timeToElapse;
    }

    public boolean isMaxAttemptsExceeded(Integer maxAttempts) {
        return maxAttempts.equals(this.attempts);
    }

    private boolean isMaxNumberOfSendingExceeded(Integer maxNumberOfSending) {
        return maxNumberOfSending.equals(this.numberOfSending);
    }

}