package az.dev.smallbankingapp.service;

import az.dev.smallbankingapp.dto.PaymentProcessorDto;
import az.dev.smallbankingapp.entity.Payment;
import az.dev.smallbankingapp.entity.PaymentType;

public interface PaymentProcessor {

    Payment process(PaymentProcessorDto paymentProcessorDto);

    PaymentType getType();

}
