package az.dev.smallbankingapp.error.model;

public enum ErrorCodes implements ErrorCode {

    SERVICE_PROVIDER_ERROR;

    @Override
    public String code() {
        return this.name();
    }

}
