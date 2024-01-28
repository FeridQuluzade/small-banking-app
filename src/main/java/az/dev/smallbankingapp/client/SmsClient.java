package az.dev.smallbankingapp.client;

import az.dev.smallbankingapp.client.model.SmsRequest;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "search-card-client",
        url = "${service.infobip.url}",
        configuration = SmsClient.FeignConfiguration.class
)
public interface SmsClient {

    @PostMapping("/sms/2/text/advanced")
    void send(@RequestBody SmsRequest smsRequest);

    class FeignConfiguration {

        @Bean
        public RequestInterceptor requestInterceptor() {
            return new AuthRequestInterceptor();
        }

    }

    class AuthRequestInterceptor implements RequestInterceptor {

        @Value("${service.infobip.api-key}")
        private String apiKey;

        @Override
        public void apply(RequestTemplate template) {
            template.header(HttpHeaders.AUTHORIZATION, apiKey);
        }

    }

}
