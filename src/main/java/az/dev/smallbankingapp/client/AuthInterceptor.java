package az.dev.smallbankingapp.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;

public class AuthInterceptor implements RequestInterceptor {

    @Value("${service.infobip.api-key}")
    private String apiKey;

    @Override
    public void apply(RequestTemplate template) {
        template.header(HttpHeaders.AUTHORIZATION, apiKey);
    }

}
