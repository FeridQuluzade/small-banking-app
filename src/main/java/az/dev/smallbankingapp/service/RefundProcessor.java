package az.dev.smallbankingapp.service;

import az.dev.smallbankingapp.dto.PaymentProcessorDto;
import az.dev.smallbankingapp.dto.request.PaymentRequest;
import az.dev.smallbankingapp.entity.Payment;
import az.dev.smallbankingapp.entity.PaymentBalanceProjection;
import az.dev.smallbankingapp.entity.PaymentType;
import az.dev.smallbankingapp.error.model.ErrorCodes;
import az.dev.smallbankingapp.error.model.ServiceException;
import az.dev.smallbankingapp.repository.PaymentRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RefundProcessor implements PaymentProcessor {

    private final PaymentRepository paymentRepository;
    private final CustomerAccountService customerAccountService;

    private static void validateOperationType(PaymentBalanceProjection paymentBalanceProjection) {
        if (!paymentBalanceProjection.is(PaymentType.PURCHASE)) {
            throw new AccessDeniedException("Operation not permitted!");
        }
    }

    @Override
    @Transactional
    public Payment process(PaymentProcessorDto paymentProcessorDto) {
        var paymentRequest = paymentProcessorDto.getPaymentRequest();
        var accountBalanceProjection = paymentProcessorDto.getBalanceProjection();
        PaymentBalanceProjection origPayment =
                paymentRepository.getProjectionById(paymentRequest.getOriginalPaymentId());

        validateOperationType(origPayment);

        if (origPayment.getAmountAfterRef().compareTo(paymentRequest.getAmount()) < 0) {
            throw ServiceException.of(ErrorCodes.NOT_ENOUGH_BALANCE);
        }

        customerAccountService.updateBalanceByAccountNumber(
                accountBalanceProjection.getAccountNumber(),
                accountBalanceProjection.addBalance(paymentRequest.getAmount()));

        subtractOriginalPaymentBalance(origPayment, paymentRequest);

        return Payment.builder()
                .paymentType(PaymentType.REFUND)
                .amount(paymentRequest.getAmount())
                .originalPaymentId(origPayment.getId())
                .destination(accountBalanceProjection.getAccountNumber())
                .build();
    }

    private void subtractOriginalPaymentBalance(PaymentBalanceProjection paymentBalanceProjection,
                                                PaymentRequest paymentRequest) {
        BigDecimal amountAfterRef =
                paymentBalanceProjection.subtractRefAmount(paymentRequest.getAmount());
        paymentRepository.updateBalanceById(paymentBalanceProjection.getId(), amountAfterRef);
    }

    @Override
    public PaymentType getType() {
        return PaymentType.REFUND;
    }

}
