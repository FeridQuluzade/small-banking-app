package az.dev.smallbankingapp.config;

import az.dev.smallbankingapp.client.SmsClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = SmsClient.class)
public class FeignConfiguration {
}
