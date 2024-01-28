package az.dev.smallbankingapp;

import az.dev.smallbankingapp.config.properties.OtpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(OtpProperties.class)
public class SmallBankingAppApplication {

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication app = new SpringApplication(SmallBankingAppApplication.class);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String serverPort = env.getProperty("local.server.port");
        log.info("Application is running in {} port, profile: {}",
                serverPort, env.getActiveProfiles());
    }

}
