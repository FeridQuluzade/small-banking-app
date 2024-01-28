package az.dev.smallbankingapp.client.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsDto {

    private List<Destination> destinations;
    private String from = "Banking App";
    private String text;

    public static SmsDto withSingleDest(String destination, String text) {
        return new SmsDto(List.of(new Destination(destination)), "Banking App", text);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Destination {
        private String to;

    }

}
