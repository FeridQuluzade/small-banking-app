package az.dev.smallbankingapp.error.model;

public interface ErrorCode {

    ErrorCode UNKNOWN_ERROR_CODE = ErrorCode.UnknownErrorCode.INSTANCE;
    ErrorCode SECURITY_CONTEXT_IS_NULL = ErrorCode.UnknownErrorCode.SECURITY_CONTEXT_IS_NULL;

    String code();

    String message();

    public static enum UnknownErrorCode implements ErrorCode {
        INSTANCE,
        SECURITY_CONTEXT_IS_NULL;

        private UnknownErrorCode() {
        }

        public String code() {
            return "unknown";
        }

        public String message() {
            return "unknown message";
        }
    }
}