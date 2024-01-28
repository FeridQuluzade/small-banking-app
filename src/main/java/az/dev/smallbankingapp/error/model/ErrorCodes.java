package az.dev.smallbankingapp.error.model;

public enum ErrorCodes implements ErrorCode {

    SERVICE_PROVIDER_ERROR(""),
    USER_NOT_FOUND(""),
    INVALID_OTP("OTP code is invalid"),
    OTP_NOT_FOUND("There is not any OTP record for phone number: {}"),
    OTP_TIME_EXPIRED("The OTP verification time has expired."),
    OTP_RESEND_UNAVAILABLE("A new OTP cannot be sent before {} seconds have elapsed"),
    OTP_VERIFICATION_BLOCKED("OTP verification has been temporarily blocked for phone number: {}");

    private final String message;

    ErrorCodes(String message) {
        this.message = message;
    }

    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String message() {
        return message;
    }

}
