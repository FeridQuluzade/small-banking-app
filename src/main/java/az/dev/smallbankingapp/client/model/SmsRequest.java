package az.dev.smallbankingapp.client.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequest {

    private List<SmsDto> messages;

    public static SmsRequest of(SmsDto smsDto) {
        return new SmsRequest(List.of(smsDto));
    }

}
