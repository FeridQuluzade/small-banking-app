package az.dev.smallbankingapp.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Setter
@Getter
@RefreshScope
@ConfigurationProperties("otp")
public class OtpProperties {

    private Integer maxNumberOfSending;
    private Integer maxNumberOfVerifying;
    private Integer otpLength;
    private Long timeToExpire;
    private Long timeToResend;
    private Long timeToLive;

}
