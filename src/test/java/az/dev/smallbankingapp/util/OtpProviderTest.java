package az.dev.smallbankingapp.util;

import az.dev.smallbankingapp.properties.TestConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OtpProviderTest {

    @Test
    void generateTest() {
        //given
        var given = new OtpProvider();

        //then
        var actual = given.generate(TestConstants.OTP_LENGTH);

        Assertions.assertEquals(actual.length(), TestConstants.OTP_LENGTH);
    }

}
