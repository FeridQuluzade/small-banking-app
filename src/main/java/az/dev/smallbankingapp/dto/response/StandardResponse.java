package az.dev.smallbankingapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardResponse {

    private String message;

    public static StandardResponse ok() {
        return new StandardResponse("success");
    }

    public static StandardResponse of(String message) {
        return new StandardResponse(message);
    }

}