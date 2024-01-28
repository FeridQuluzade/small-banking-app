package az.dev.smallbankingapp.util;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class OtpProvider {

    public String generate(int length) {
        String numbers = "0123456789";
        SecureRandom random = new SecureRandom();

        char[] otp = new char[length];

        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(random.nextInt(numbers.length()));
        }

        return new String(otp);
    }

}
