package az.dev.smallbankingapp.service;

import az.dev.smallbankingapp.dto.PaymentProcessorDto;
import az.dev.smallbankingapp.dto.request.PaymentRequest;
import az.dev.smallbankingapp.entity.AccountBalanceProjection;
import az.dev.smallbankingapp.entity.Payment;
import az.dev.smallbankingapp.entity.PaymentType;
import az.dev.smallbankingapp.error.model.ErrorCodes;
import az.dev.smallbankingapp.error.model.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PurchaseProcessor implements PaymentProcessor {

    private final CustomerAccountService customerAccountsRepository;

    @Override
    @Transactional
    public Payment process(PaymentProcessorDto paymentProcessorDto) {
        PaymentRequest paymentRequest = paymentProcessorDto.getPaymentRequest();
        AccountBalanceProjection accountBalanceProjection =
                paymentProcessorDto.getBalanceProjection();

        if (accountBalanceProjection.getBalance().compareTo(paymentRequest.getAmount()) < 0) {
            throw ServiceException.of(ErrorCodes.NOT_ENOUGH_BALANCE);
        }

        customerAccountsRepository.updateBalanceByAccountNumber(
                accountBalanceProjection.getAccountNumber(),
                accountBalanceProjection.subtractBalance(paymentRequest.getAmount()));

        return Payment.builder()
                .paymentType(PaymentType.PURCHASE)
                .source(accountBalanceProjection.getAccountNumber())
                .destination(paymentRequest.getDestination())
                .amount(paymentRequest.getAmount())
                .build();
    }

    @Override
    public PaymentType getType() {
        return PaymentType.PURCHASE;
    }

}
