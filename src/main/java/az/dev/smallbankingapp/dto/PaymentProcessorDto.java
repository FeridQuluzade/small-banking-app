package az.dev.smallbankingapp.dto;

import az.dev.smallbankingapp.dto.request.PaymentRequest;
import az.dev.smallbankingapp.entity.AccountBalanceProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProcessorDto {

    private AccountBalanceProjection balanceProjection;
    private PaymentRequest paymentRequest;

}
