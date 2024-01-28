package az.dev.smallbankingapp.service;

import az.dev.smallbankingapp.entity.PaymentType;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PaymentProcessorFactory {

    private final Map<PaymentType, PaymentProcessor> paymentProcessorMap;

    public PaymentProcessorFactory(List<PaymentProcessor> paymentProcessors) {
        paymentProcessorMap = paymentProcessors.stream()
                .collect(Collectors.toMap(PaymentProcessor::getType, Function.identity()));
    }

    public PaymentProcessor getProcessorByType(PaymentType type) {
        return paymentProcessorMap.get(type);
    }

}
