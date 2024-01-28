package az.dev.smallbankingapp.error.validation;

public final class Patterns {

    public static final String PHONE_REGEX = "(^(994)(50|51|55|60|70|77|99|10)\\d{7}$)|(^$)";
    public static final String OTP = "^[0-9]+$";
    public static final String TEMPLATE = "^(?=.{0,158}$)[ -Za-z\r\n]*\\{\\}[ -Za-z\r\n]*$";

    private Patterns() {
    }

}