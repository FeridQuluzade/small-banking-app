package az.dev.smallbankingapp.service;

import az.dev.smallbankingapp.dto.PaymentProcessorDto;
import az.dev.smallbankingapp.entity.Payment;
import az.dev.smallbankingapp.entity.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TopUpProcessor implements PaymentProcessor {

    private final CustomerAccountService customerAccountsRepository;

    @Override
    @Transactional
    public Payment process(PaymentProcessorDto paymentProcessorDto) {
        var paymentRequest = paymentProcessorDto.getPaymentRequest();
        var accountBalanceProjection = paymentProcessorDto.getBalanceProjection();

        customerAccountsRepository.updateBalanceByAccountNumber(
                accountBalanceProjection.getAccountNumber(),
                accountBalanceProjection.addBalance(paymentRequest.getAmount())
        );

        return Payment.builder()
                .paymentType(PaymentType.TOP_UP)
                .source(paymentRequest.getSource())
                .destination(accountBalanceProjection.getAccountNumber())
                .amount(paymentRequest.getAmount())
                .build();
    }

    @Override
    public PaymentType getType() {
        return PaymentType.TOP_UP;
    }

}
