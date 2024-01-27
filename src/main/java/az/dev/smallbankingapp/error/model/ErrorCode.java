package az.dev.smallbankingapp.error.model;

public interface ErrorCode {

    ErrorCode UNKNOWN_ERROR_CODE = UnknownErrorCode.INSTANCE;
    ErrorCode SECURITY_CONTEXT_IS_NULL = UnknownErrorCode.SECURITY_CONTEXT_IS_NULL;

    String code();

    enum UnknownErrorCode implements ErrorCode {

        INSTANCE,
        SECURITY_CONTEXT_IS_NULL;

        @Override
        public String code() {
            return "unknown";
        }
    }

}