package az.dev.smallbankingapp.dto.request;

import az.dev.smallbankingapp.entity.PaymentType;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull
    private BigDecimal amount;
    private String destination;
    private String source;
    private Long originalPaymentId;

    @NotNull
    private PaymentType paymentType;

}
