package az.dev.smallbankingapp.properties;

import java.time.LocalDateTime;

public final class TestConstants {

    public static final String GSM_NUMBER = "994103456789";
    public static final String UUID = "837c76f8-2a57-4d74-9611-5e0bbde849a1";
    public static final String ID = "994103456789";
    public static final String OTP_CODE = "1234";
    public static final Integer ATTEMPTS = 0;
    public static final Integer FIRST_TIME_SENDING = 1;
    public static final LocalDateTime SENT_TIME = LocalDateTime.now();
    public static final Integer NUMBER_OF_SENDING = 0;
    public static final Long TIME_TO_LIVE = 900L;
    public static final Long TIME_TO_EXPIRE = 180L;
    public static final Long TIME_TO_RESEND = 180L;
    public static final int OTP_LENGTH = 4;

    private TestConstants() {
    }

}
