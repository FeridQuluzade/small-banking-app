package az.dev.smallbankingapp.service;

import az.dev.smallbankingapp.dto.PaymentProcessorDto;
import az.dev.smallbankingapp.dto.request.PaymentRequest;
import az.dev.smallbankingapp.dto.response.PaymentResponse;
import az.dev.smallbankingapp.entity.AccountBalanceProjection;
import az.dev.smallbankingapp.entity.Payment;
import az.dev.smallbankingapp.entity.UserType;
import az.dev.smallbankingapp.repository.PaymentRepository;
import az.dev.smallbankingapp.util.RequestContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProcessorFactory paymentProcessorFactory;
    private final CustomerAccountService customerAccountService;

    @Transactional
    public PaymentResponse process(PaymentRequest paymentRequest) {
        checkUserPermission();

        AccountBalanceProjection projection =
                customerAccountService.findProjectionByGsmNumber(RequestContextUtil.getUsername());
        PaymentProcessor paymentProcessor =
                paymentProcessorFactory.getProcessorByType(paymentRequest.getPaymentType());

        var payment = paymentProcessor.process(new PaymentProcessorDto(projection, paymentRequest));
        Payment afterSave = paymentRepository.save(payment);

        return PaymentResponse.of(afterSave.getId());
    }

    private void checkUserPermission() {
        if (UserType.NON_VERIFIED.equals(RequestContextUtil.getUserType())) {
            throw new AccessDeniedException("Operation not permitted!");
        }
    }

}
