package az.dev.smallbankingapp.service;

import az.dev.smallbankingapp.dto.PaymentProcessorDto;
import az.dev.smallbankingapp.entity.Payment;
import az.dev.smallbankingapp.entity.PaymentType;
import org.springframework.stereotype.Component;

@Component
public class RefundProcessor implements PaymentProcessor {

    @Override
    public Payment process(PaymentProcessorDto paymentProcessorDto) {
        return null;
    }

    @Override
    public PaymentType getType() {
        return PaymentType.REFUND;
    }

}
